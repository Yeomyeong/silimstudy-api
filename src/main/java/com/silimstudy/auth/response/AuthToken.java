package com.silimstudy.auth.response;

import com.silimstudy.auth.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Getter
@AllArgsConstructor
public class AuthToken {
    private static final AuthToken notMatchedToken = new AuthToken(LoginResponse.NOT_MATCHED);
    private static final AuthToken notValidToken = new AuthToken(LoginResponse.NOT_VALID);

    private String username;
    private Collection<Authority> authorities;
    private String token;
    private LoginResponse response;

    private AuthToken(LoginResponse response){
        this.response = response;
    }

    public static AuthToken notMatchedToken() {
        return notMatchedToken;
    }

    public static AuthToken notValidToken() {
        return notValidToken;
    }

    public static AuthToken successToken(String username, Collection<Authority> authorities, String token) {
        AuthToken that = new AuthToken(LoginResponse.SUCCESS);
        that.username = username;
        that.authorities = authorities;
        that.token = token;
        return that;
    }

    public enum LoginResponse{
        SUCCESS, NOT_MATCHED, NOT_VALID
    }
}
