package com.wmiii.video.service;

import com.wmiii.video.entity.CourseVideo;
import com.wmiii.video.entity.VideoStructure;
import com.wmiii.video.params.CourseVideoParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.VideoStructureParam;

public interface VideoStructureService {
    Result storeStructure(VideoStructureParam videoStructureParam);

    Result getStructure(Integer courseId);

    Result deleteStructure(CourseVideoParam courseVideoParam, String token);
}
