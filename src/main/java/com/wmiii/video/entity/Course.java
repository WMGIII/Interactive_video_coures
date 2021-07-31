package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Course {
    @TableId(value = "courseId")
    private Integer courseId;
    private String courseName;
    private String courseIntro;
    private Integer teacherId;
    private String teacherName;
    private Long createDate;
}
