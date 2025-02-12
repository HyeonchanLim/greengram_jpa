package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title="로그인 응답")
public class UserSignInRes {
    private long userId;
    private String nickName;
    private String pic;
    private String accessToken;
}
