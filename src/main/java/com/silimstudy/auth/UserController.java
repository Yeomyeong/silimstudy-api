package com.silimstudy.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
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
    public AuthToken login(LoginRequest loginRequest, HttpSession session) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.readUser(username);
        if (!userService.passwordEncoder().matches(password, user.getPassword()))
            return null;

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
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
