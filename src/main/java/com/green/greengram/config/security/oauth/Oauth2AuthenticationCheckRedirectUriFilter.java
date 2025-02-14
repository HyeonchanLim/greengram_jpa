package com.green.greengram.config.security.oauth;

import com.green.greengram.common.GlobalOauth2;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationCheckRedirectUriFilter extends OncePerRequestFilter {
    private final GlobalOauth2 globalOauth2;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 호스트 주소값을 제외하고 요청한 URI
        // ex) http://localhost:8080/oauth2/authorization?redirect_uri=abc
        // 호스트 주소값 : http://localhost:8080
        // 제외하고 요청한 URI : /oauth2/authorization
        //redirectUri = abc;
        String requestUri = request.getRequestURI();
        log.info("request URI : {}",requestUri);
        // requestUri 주소 뒤로 쿼리스트링 방식으로 들어오기 때문에 startsWith 사용
        if (requestUri.startsWith(globalOauth2.getBaseUri())){ // 소셜 로그인 요청한 것이라면
            String redirectUri = request.getParameter("redirect_uri");
            if (redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)){// 약속한 redirect_uri 값이 아니었다면
                String errRedirectUri = UriComponentsBuilder.fromUriString("/err_message")
                        .queryParam("message","유효한 Redirect URL이 아닙니다.")
                        .encode()
                        .toUriString();

                //errRedirectUrl = "/err_message?message=유효한 Redirect URL 이 아닙니다."
                response.sendRedirect(errRedirectUri);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
    //약속한 redirect_uri 가 맞는지 체크 -> 없으면 false , 있으면 true 리턴
    private boolean hasAuthorizedRedirectUri(String redirectUri){
        for (String uri : globalOauth2.getAuthorizedRedirectUris()){
            if (uri.equals(redirectUri)){
                return  true;
            }
        }
        return false;
    }
}
