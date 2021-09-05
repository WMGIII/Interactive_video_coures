package com.wmiii.video.service;

import com.wmiii.video.params.CourseVideoIdParam;
import com.wmiii.video.params.CourseVideoParam;
import com.wmiii.video.params.Result;

public interface OptionDataService {
    Result makeChoice(String token, Integer courseId, Integer videoId);

    Result getChoiceData(String token, CourseVideoIdParam param);
}
