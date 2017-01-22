package com.silimstudy.study;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yeomyeongwoo on 2017. 1. 6..
 */
@Entity
@Data
@NoArgsConstructor
public class Study {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private String title;
    private String contents;

    public Study(String title, String contents, long userId) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
