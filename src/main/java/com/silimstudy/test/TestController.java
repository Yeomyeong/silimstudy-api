package com.silimstudy.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
@RestController
public class TestController {
    @RequestMapping(path = "/test")
    public String test() {
        return "Hello Spring Boot!";
    }
}
