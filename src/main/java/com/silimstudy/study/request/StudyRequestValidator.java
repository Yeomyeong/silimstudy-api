package com.silimstudy.study.request;

import com.silimstudy.auth.request.JoinRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by yeojung on 17. 1. 14.
 */
public class StudyRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == StudyRequest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "title", "title.empty");
        ValidationUtils.rejectIfEmpty(errors, "contents", "contents.empty");
    }
}
