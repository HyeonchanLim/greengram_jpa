package com.green.greengram;

import com.green.greengram.config.jwt.JwtUser;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) // life-cycle 지정
@WithSecurityContext(factory = WithAuthMockUserSecurityContextFactory.class)
public @interface WithAuthUser {
    long signedUserId() default 1L;
    String[] roles() default  {"ROLE_USER", "ROLE_ADMIN"};


}
