package com.wmiii.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class CourseVideo {
    @TableId(value = "videoId", type = IdType.AUTO)
    Integer videoId;
    String videoName;
    String videoIntro;
    Integer teacherId;
    Integer courseId;
    Integer optionId;
    String optionInfo;
    String nextVideo;
}
