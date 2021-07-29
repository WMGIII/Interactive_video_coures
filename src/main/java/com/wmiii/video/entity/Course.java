package com.wmiii.video.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Course {
    @Id
    private Integer courseId;
    private String courseName;
    private String courseIntro;
    private Integer teacherId;
    private String teacherName;
}
