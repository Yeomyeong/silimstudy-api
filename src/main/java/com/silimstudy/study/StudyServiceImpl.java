package com.silimstudy.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
@Service
public class StudyServiceImpl implements StudyService {
    private StudyRepository studyRepository;

    @Autowired
    public StudyServiceImpl(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Override
    public List<Study> search() {
        return studyRepository.findAll();
    }

    @Override
    public void register(StudyRegisterRequest request) {
        studyRepository.save(new Study(request.getTitle(), request.getContents()));
    }
}
