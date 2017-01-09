package com.silimstudy.study;

import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
public interface StudyService {
    void register(StudyRegisterRequest request);
    List<Study> search();
}
