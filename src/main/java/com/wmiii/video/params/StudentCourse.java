package com.wmiii.video.params;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class StudentCourse {
    @TableId(value = "Id", type = IdType.AUTO)
    Integer Id;
    Integer CourseId;
    Integer StudentId;
}
