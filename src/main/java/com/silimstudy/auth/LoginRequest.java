package com.silimstudy.auth;

import lombok.Data;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
