package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Teacher {
    @TableId(value = "teacherId")
    private Integer teacherId;
    private String email;
    private String pwd;
    private String teacherName;
    private String teacherIntro;
    private Long createDate;
    private Long lastLogin;
}
