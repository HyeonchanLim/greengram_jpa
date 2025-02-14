package com.green.greengram.config.security.oauth.userInfo;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.GlobalOauth2;
import com.green.greengram.config.security.oauth.Oauth2AuthenticationRequestBasedOnCookieRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Component
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final CookieUtils cookieUtils;
    private final GlobalOauth2 globalOauth2;

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException {
        exception.printStackTrace();

        // FE - Redirect-Url 획득 from cookie
        String redirectUrl = cookieUtils.getValue(req,globalOauth2.getRedirectUriParamCookieName(),String.class);

        // URL 에 에러 쿼리스트링 추가
        String targetUrl = redirectUrl == null ? "/" : UriComponentsBuilder
                .fromUriString(redirectUrl)
                .queryParam("error",exception.getLocalizedMessage())
                .build()
                .toUriString();

        // targetUrl = "http://localhost:8080/fe/redirect?error=어저고저쩌고";
        getRedirectStrategy().sendRedirect(req, res,redirectUrl);
    }
}

