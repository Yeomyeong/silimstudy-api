package com.silimstudy.study;

import com.silimstudy.auth.User;
import com.silimstudy.study.request.StudyRequest;

import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
public interface StudyService {
    void register(User user, StudyRequest request);
    List<Study> search();
    List<Study> search(long userId);
}
