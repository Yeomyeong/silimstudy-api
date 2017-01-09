package com.silimstudy.study;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
public class StudyServiceImplInMemory implements StudyService {
    private static List<Study> studies = new ArrayList<>();

    @Override
    public void register(StudyRegisterRequest request) {
        studies.add(new Study(request.getTitle(), request.getContents()));
    }

    @Override
    public List<Study> search() {
        return studies;
    }
}
