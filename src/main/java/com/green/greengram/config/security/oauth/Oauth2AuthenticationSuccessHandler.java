package com.green.greengram.config.security.oauth;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.GlobalOauth2;
import com.green.greengram.config.jwt.JwtUser;
import com.green.greengram.config.jwt.TokenProvider;
import com.green.greengram.config.security.MyUserDetails;
import com.green.greengram.config.security.oauth.userInfo.Oauth2JwtUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final TokenProvider tokenProvider;
    private final GlobalOauth2 globalOauth2;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess (HttpServletRequest req , HttpServletResponse res, Authentication auth)
            throws IOException, ServletException {
        if (res.isCommitted()){ // 응답 객체가 만료된 경우 ( 이전 프로세스에서 응답처리를 했는 상태 )
            log.error("onAuthenticationSuccess called with a committed response {}" , res);
            return;
        }
        String targetUrl = "";
        log.info("onAuthenticationSuccess targetUrl = {}" , targetUrl);
        clearAuthenticationAttributes(req, res);
        getRedirectStrategy().sendRedirect(req,res,targetUrl); // "fe/redirect?access_token=ddd&user_id=12"
    }
    @Override
    protected String determineTargetUrl(HttpServletRequest req,HttpServletResponse res,Authentication auth) {
        String redirectUrl = cookieUtils.getValue(req,globalOauth2.getRedirectUriParamCookieName(),String.class);
        log.info("determineTargetUrl > getDefaultTargetUrl () : {}", getDefaultTargetUrl());
        String targetUrl = redirectUrl == null ? getDefaultTargetUrl() : redirectUrl;
        // 쿼리스트링 생성
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Oauth2JwtUser oauth2JwtUser = (Oauth2JwtUser) myUserDetails.getJwtUser();

        JwtUser jwtUser = new JwtUser(oauth2JwtUser.getSignedUserId(),oauth2JwtUser.getRoles());

        // AT , RT 생성
        String accessToken = tokenProvider.generateToken(jwtUser, Duration.ofMinutes(10));
        String refreshToken = tokenProvider.generateToken(jwtUser,Duration.ofDays(1));

        int maxAge = 1_296_000; //
        cookieUtils.setCookie(res,"refreshToken",refreshToken,maxAge,"api/user/access-token");

        // 쿼리스트링 생성
        // targetUrl : /fe/redirect
        // accessToken : aaa
        // userId : 20
        // NickName : 홍길동
        // pic : abc.jpg -- 처럼 값이 있다 가정 -> fe/redirect?access_token=aaa&user_id=20&nick_name=홍길동&pic=abc.jpg
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token",accessToken)
                .queryParam("user_id",oauth2JwtUser.getSignedUserId())
                .queryParam("nick_name",oauth2JwtUser.getNickName()).encode()
                .queryParam("pic",oauth2JwtUser.getPic())
                .build()
                .toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest req, HttpServletResponse res){
        super.clearAuthenticationAttributes(req);
        repository.removeAuthorizationCookies(res);
    }

}
