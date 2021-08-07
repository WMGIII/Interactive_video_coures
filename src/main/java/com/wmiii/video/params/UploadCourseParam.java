package com.wmiii.video.params;

import lombok.Data;

import java.util.List;

@Data
public class UploadCourseParam {
    Integer courseId;
    List<UploadVideoParam> videos;
}
