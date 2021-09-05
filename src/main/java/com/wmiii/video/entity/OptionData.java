package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class OptionData {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Integer courseId;
    Integer videoId;
    Integer studentId;
    Long time;
}
