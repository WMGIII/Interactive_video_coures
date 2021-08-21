package com.wmiii.video.service;

import com.wmiii.video.entity.VideoStructure;
import com.wmiii.video.params.Result;

public interface VideoStructureService {
    Result storeStructure(VideoStructure videoStructure);

    Result getStructure(Integer courseId, Integer videoId);
}
