package com.wmiii.video.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Student {
    // @Id
    private Integer studentId;
    private String email;
    private String pwd;
    private String studentName;
    private String studentNumber;
    private Long createDate;
    private Long lastLogin;
}
