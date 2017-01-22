package com.silimstudy.study;

import com.silimstudy.auth.User;
import com.silimstudy.study.request.StudyRequest;
import com.silimstudy.study.request.StudyRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
@RestController
@RequestMapping(path = "/study")
public class StudyController {
    @Autowired
    private StudyService studyService;

    @RequestMapping(method = RequestMethod.POST)
    public String register(User user, StudyRequest request, Errors errors) {
        new StudyRequestValidator().validate(request, errors);
        if (errors.hasErrors())
            return "FAIL";

        studyService.register(user, request);
        return "SUCCESS";
    }

    @RequestMapping(path = "/my/list", method = RequestMethod.GET)
    public @ResponseBody List<Study> myList(User user) {
        return studyService.search(user.getId());
    }

    @RequestMapping(path = "/all/list", method = RequestMethod.GET)
    public @ResponseBody List<Study> list() {
        return studyService.search();
    }
}
