package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Student {
    @TableId(value = "studentId")
    private Integer studentId;
    private String email;
    private String pwd;
    private String studentName;
    private String studentNumber;
    private Long createDate;
    private Long lastLogin;
}
