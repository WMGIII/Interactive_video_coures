package com.wmiii.video.service;

import com.wmiii.video.params.Result;
import com.wmiii.video.params.UploadCourseParam;

public interface CourseVideoService {
    Result findVideoByVideoId(Integer videoId);

    Result submit(UploadCourseParam uploadCourseParam, String token);


}
