package com.silimstudy.auth.request;

import lombok.Data;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Data
public class JoinRequest {
    private String username;
    private String password;
    private String email;

}
