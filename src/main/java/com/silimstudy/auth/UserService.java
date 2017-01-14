package com.silimstudy.auth;

import com.silimstudy.auth.request.JoinRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * Created by yeojung on 17. 1. 14.
 */
public interface UserService extends UserDetailsService{
    Collection<GrantedAuthority> getAuthorities(String username);
    User readUser(String username);
    void createUser(JoinRequest user);
    void deleteUser(String username);
    PasswordEncoder passwordEncoder();
}
