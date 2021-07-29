package com.wmiii.video.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Teacher {
    // @Id
    private Integer teacherId;
    private String email;
    private String pwd;
    private String teacherName;
    private String teacherIntro;
    private Long createDate;
    private Long lastLogin;
}
