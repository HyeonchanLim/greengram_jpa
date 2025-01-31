package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="로그인 응답")
@EqualsAndHashCode
@AllArgsConstructor
public class UserSignInRes {
    private long userId;
    private String nickName;
    private String pic;
    private String accessToken;

}
