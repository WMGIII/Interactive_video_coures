package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class WatchTime {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Integer studentId;
    Integer courseId;
    Integer videoId;
    Integer mTime;
}
