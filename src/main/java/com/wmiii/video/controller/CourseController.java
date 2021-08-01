package com.wmiii.video.controller;

import com.wmiii.video.params.CourseParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public Result courses() {
        return courseService.getAllCourses(1);
    }

    @PostMapping("/create_course")
    public Result createCourse() {
        return Result.success(null);
    }

    @PostMapping("/create_course/submit")
    public Result submit(@RequestHeader(value="Authorization", required = false) String token, @RequestBody CourseParam courseParam) {
        return courseService.submit(courseParam, token);
    }

    @PostMapping("/{courseId}")
    public Result course(@PathVariable Integer courseId) {
        return courseService.findCourseById(courseId);
    }

    @PostMapping("/{courseId}/join")
    public Result joinCourse(@PathVariable Integer courseId) {
        return courseService.joinCourse(courseId);
    }
}
