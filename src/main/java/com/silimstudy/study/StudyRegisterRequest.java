package com.silimstudy.study;

import lombok.Data;
import lombok.ToString;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
@Data
@ToString
public class StudyRegisterRequest {
    private String title;
    private String contents;
}
