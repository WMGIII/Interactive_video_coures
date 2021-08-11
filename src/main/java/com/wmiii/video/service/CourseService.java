package com.wmiii.video.service;

import com.wmiii.video.params.CourseParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.StudentCourse;


public interface CourseService {
    Result findCourseById(Integer courseId);

    Result submit(CourseParam courseParam, String token);

    Result findCourseByTeacherId(Integer teacherId);

    Result joinCourse(Integer courseId, String token);

    Result getAllCourses(Integer limit);

    StudentCourse findStudentCourse(Integer studentId, Integer courseId);
}
