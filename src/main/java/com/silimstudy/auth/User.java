package com.silimstudy.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Entity
@Data
@NoArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;
    private String email;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> authorities;

    public static User createUser(String username, String password, String email) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.email = email;
        user.isAccountNonExpired = true;
        user.isAccountNonLocked = true;
        user.isCredentialsNonExpired = true;
        user.isEnabled = true;

        return user;
    }
}
