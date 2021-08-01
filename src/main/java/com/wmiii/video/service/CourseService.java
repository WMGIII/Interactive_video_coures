package com.wmiii.video.service;

import com.wmiii.video.params.CourseParam;
import com.wmiii.video.params.Result;


public interface CourseService {
    Result findCourseById(Integer courseId);

    Result submit(CourseParam courseParam, String token);

    Result findCourseByTeacherId(Integer teacherId);

    Result joinCourse(Integer courseId, String token);

    Result getAllCourses(Integer limit);
}
