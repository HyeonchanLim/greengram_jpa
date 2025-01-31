package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserSignUpReq {

    @Schema(description = "유저 아이디", example = "mic", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(description = "유저 비밀번호", example = "1212", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
    @Schema(description = "유저 닉네임", example = "홍길동")
    private String nickName;
}
