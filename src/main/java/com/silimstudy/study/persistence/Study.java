package com.silimstudy.study.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Study {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long adminId;
//    private List<Long> participantIds;
    private String title;
    private String contents;
    private boolean isActive;

    public Study(String title, String contents, long adminId) {
        this.adminId = adminId;
        this.title = title;
        this.contents = contents;
        this.isActive = true;
    }
}
