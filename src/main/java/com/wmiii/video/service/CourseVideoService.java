package com.wmiii.video.service;

import com.wmiii.video.entity.CourseVideo;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.UploadCourseParam;

public interface CourseVideoService {
    Result findVideoByVideoId(Integer videoId);

    Result submit(UploadCourseParam uploadCourseParam, String token);

    Integer storeVideo(String videoName, Integer courseId, Integer teacherId);

    Integer getVideoIdByOriginName(Integer courseId, String videoName);

    CourseVideo findVideoByVideoName(String videoName, Integer courseId);

    Integer deleteByVideoId(Integer videoId);

    Boolean setBlankStructure(Integer courseId);
}
