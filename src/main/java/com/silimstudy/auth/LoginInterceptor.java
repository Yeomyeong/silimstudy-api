package com.silimstudy.auth;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            SecurityContext context = (SecurityContextImpl) request.getSession()
                    .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

            if ( isUserOrAdmin(context) )
                return true;
            else {
                response.setStatus(401);
                return false;
            }

    }

    private boolean isUserOrAdmin(SecurityContext context) {
        if (context == null)
            return false;
        Authentication authentication = context.getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null)
            return false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (Authority.Name.USER.name().equals(authority.getAuthority())
                    || Authority.Name.ADMIN.name().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
