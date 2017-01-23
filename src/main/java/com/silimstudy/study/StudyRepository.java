package com.silimstudy.study;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
public interface StudyRepository extends JpaRepository<Study, Long>{
    List<Study> findByAdminId(Long adminId);

}
