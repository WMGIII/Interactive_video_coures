package com.wmiii.video.service;

import com.wmiii.video.params.CourseVideoIdParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.WatchTimeParam;

public interface WatchTimeService {
    Result setTime(WatchTimeParam watchTimeParam, String token);

    Result getTimes(CourseVideoIdParam param);
}
