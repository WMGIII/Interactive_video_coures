package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CourseStructure {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    Integer courseId;
    String jsonStructure;
    Long lastChange;
}
