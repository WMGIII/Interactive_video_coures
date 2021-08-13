package com.wmiii.video.service;

import com.wmiii.video.entity.CourseVideo;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.UploadCourseParam;
import com.wmiii.video.params.UploadVideoParam;

import java.util.List;

public interface CourseVideoService {
    Result findVideoByVideoId(Integer videoId, String token);

    Result submit(UploadVideoParam uploadVideoParam, String token);

    Integer storeVideo(String videoName, Integer courseId, Integer teacherId, String fileType, Boolean isRoot);

    Integer getVideoIdByOriginName(Integer courseId, String videoName);

    CourseVideo findVideoByVideoName(String videoName, Integer courseId);

    Integer deleteByVideoId(Integer videoId);

    Boolean setBlankStructure(Integer videoId, Integer courseId, String fileType);

    // Result getStructureByCourseId(Integer courseId, String token);

    Result getVideoList(Integer courseId, String token);

    Integer setUrl(Integer videoId, String fileType);

    Result getRootVideo(Integer courseId, String token);
}
