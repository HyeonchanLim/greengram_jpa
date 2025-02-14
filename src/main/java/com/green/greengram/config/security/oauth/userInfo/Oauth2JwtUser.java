package com.green.greengram.config.security.oauth.userInfo;

import com.green.greengram.config.jwt.JwtUser;
import lombok.Getter;

import java.util.List;

@Getter
public class Oauth2JwtUser extends JwtUser {
    private final String nickName;
    private final String pic;

    public Oauth2JwtUser(String nickName, String pic , long signedUserId , List<String> roles){
        super(signedUserId, roles);
        this.nickName = nickName;
        this.pic = pic;
    }
}
