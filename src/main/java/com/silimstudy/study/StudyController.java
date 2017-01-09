package com.silimstudy.study;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(StudyRegisterRequest request) {

        studyService.register(request);

        return request.toString();
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public @ResponseBody List<Study> list() {
        return studyService.search();
    }
}
