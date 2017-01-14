package com.silimstudy.auth;

import com.silimstudy.auth.request.JoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAuthorityRepository userAuthorityRepository) {
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readUser(username);
    }

    @Override
    public User readUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void createUser(JoinRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null)
            throw new AlreadyExistingUserException();
        String encodedPw = passwordEncoder.encode(request.getPassword());
        User newUser = User.createUser(request.getUsername(), encodedPw, request.getEmail());
        userRepository.save(newUser);
        userAuthorityRepository.save(new Authority(Authority.Name.USER, newUser.getId()));
    }

    @Override
    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
