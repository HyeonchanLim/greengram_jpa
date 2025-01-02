package com.green.greengram.user.follow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@ToString
@EqualsAndHashCode // 이게 있어야 들어오는 값이랑 비교해서 동등성 체크 가능
public class UserFollowReq {
    @JsonIgnore
    @Setter // 특정 멤버필드만 setter 로 지정 가능
    private long fromUserId;

    @JsonProperty("to_user_id")
    @Schema(name = "to_user_id", description = "팔로잉 유저 ID", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long toUserId;

    @ConstructorProperties({"to_user_id"})
    public UserFollowReq(long toUserId) {
        this.toUserId = toUserId;
    }


}
