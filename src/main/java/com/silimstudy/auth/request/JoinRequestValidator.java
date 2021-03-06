package com.silimstudy.auth.request;

import com.silimstudy.auth.request.JoinRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by yeojung on 17. 1. 14.
 */
public class JoinRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == JoinRequest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "username", "username.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "email.empty");
    }
}
