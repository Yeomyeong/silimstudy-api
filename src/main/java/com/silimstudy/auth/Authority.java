package com.silimstudy.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Entity
@Data
@NoArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String authorityName;

    public Authority(String username, Name authorityName) {
        this.username = username;
        this.authorityName = authorityName.name();
    }

    public enum Name {
        USER, ADMIN;
    }
}
