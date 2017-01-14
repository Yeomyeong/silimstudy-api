package com.silimstudy.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Getter
@AllArgsConstructor
public class AuthToken {
    private static AuthToken nonetoken = new AuthToken();

    private String username;
    private Collection<Authority> authorities;
    private String token;

    private AuthToken(){}

    public static AuthToken none() {
        return nonetoken;
    }
}
