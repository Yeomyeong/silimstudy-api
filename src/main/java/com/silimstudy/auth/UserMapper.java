package com.silimstudy.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Service
public class UserMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;

    public User readUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<String> readAuthorityNames(String username) {
        List<String> result = new ArrayList<>();
        authRepository.findByUsername(username).forEach(authority -> result.add(authority.getAuthorityName()));
        return result;

    }
}
