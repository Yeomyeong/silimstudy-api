package com.silimstudy.auth;

import com.silimstudy.auth.request.JoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readUser(username);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authRepository.findByUsername(username)
                .forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth.getAuthorityName())));
        return authorities;
    }

    @Override
    public User readUser(String username) {
        User user = userRepository.findByUsername(username);
        user.setAuthorities(getAuthorities(username));
        return user;
    }

    @Override
    public void createUser(JoinRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null)
            throw new AlreadyExistingUserException();
        String encodedPw = passwordEncoder.encode(request.getPassword());
        userRepository.save(new User(request.getUsername(), encodedPw, request.getEmail()));
        authRepository.save(new Authority(request.getUsername(), Authority.Name.USER));
    }

    @Override
    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
        authRepository.delete(authRepository.findByUsername(username));
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
