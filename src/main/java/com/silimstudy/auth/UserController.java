package com.silimstudy.auth;

import com.silimstudy.auth.request.JoinRequest;
import com.silimstudy.auth.request.JoinRequestValidator;
import com.silimstudy.auth.request.LoginRequest;
import com.silimstudy.auth.request.LoginRequestValidator;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method= RequestMethod.POST)
    public AuthToken login(LoginRequest request, HttpSession session, Errors errors) {
        new LoginRequestValidator().validate(request, errors);
        if (errors.hasErrors())
            return AuthToken.none();

        User user = userService.readUser(request.getUsername());
        if (!userService.passwordEncoder().matches(request.getPassword(), user.getPassword()))
             return AuthToken.none();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return new AuthToken(user.getUsername(), user.getAuthorities(), session.getId());
    }

    @RequestMapping(path = "/join", method= RequestMethod.POST)
    public String join(JoinRequest request, Errors errors) {
        new JoinRequestValidator().validate(request, errors);
        if (errors.hasErrors())
            return "fail";
        try {
            userService.createUser(request);
            return "success";
        } catch (AlreadyExistingUserException e) {
            errors.rejectValue("username", "duplicated");
            return "fail";
        }
    }
}
