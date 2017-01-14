package com.silimstudy.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Entity
@Data
@NoArgsConstructor
public class Authority implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_id")
    private long userId;
    private String authority;

    public Authority(Name authorityName) {
        this.authority = authorityName.name();
    }
    public Authority(Name authorityName, long userId) {
        this.authority = authorityName.name();
        this.userId = userId;
    }

    public enum Name {
        USER, ADMIN;
    }
}
