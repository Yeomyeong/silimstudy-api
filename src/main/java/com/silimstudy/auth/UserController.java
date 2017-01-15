package com.silimstudy.auth;

import com.silimstudy.auth.request.JoinRequest;
import com.silimstudy.auth.request.JoinRequestValidator;
import com.silimstudy.auth.request.LoginRequest;
import com.silimstudy.auth.request.LoginRequestValidator;
import com.silimstudy.auth.response.AuthToken;
import com.silimstudy.auth.response.JoinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by yeojung on 17. 1. 14.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @RequestMapping(path = "/join", method= RequestMethod.POST)
    public JoinResponse join(JoinRequest request, Errors errors) {
        new JoinRequestValidator().validate(request, errors);
        if (errors.hasErrors())
            return JoinResponse.NOT_VALID;
        try {
            userService.createUser(request);
            return JoinResponse.SUCCESS;
        } catch (AlreadyExistingUserException e) {
            return JoinResponse.ALREADY_EXIST;
        }
    }

    @RequestMapping(path = "/login", method= RequestMethod.POST)
    public AuthToken login(LoginRequest request, HttpSession session, Errors errors) {
        new LoginRequestValidator().validate(request, errors);
        if (errors.hasErrors())
            return AuthToken.notValidToken();

        User user = userService.readUser(request.getUsername());
        if ( user == null || !userService.passwordEncoder().matches(request.getPassword(), user.getPassword()))
             return AuthToken.notMatchedToken();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return AuthToken.successToken(user.getUsername(), user.getAuthorities(), session.getId());
    }

    //TODO 회원 탈퇴 기능

    //TODO 회원 정보 수정 기능

}
