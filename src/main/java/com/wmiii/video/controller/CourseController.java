package com.wmiii.video.controller;

import com.wmiii.video.params.CourseParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.UploadCourseParam;
import com.wmiii.video.params.UploadVideoParam;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.CourseVideoService;
import com.wmiii.video.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoService courseVideoService;

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
    public Result joinCourse(@RequestHeader(value="Authorization", required = false) String token, @PathVariable Integer courseId) {
        return courseService.joinCourse(courseId, token);
    }

    @PostMapping("/{courseId}/video/submit")
    public Result submitVideos(@RequestHeader(value="Authorization", required = false) String token, @PathVariable Integer courseId, @RequestBody UploadVideoParam uploadVideoParam) {
        return courseVideoService.submit(uploadVideoParam, token);
    }

    @PostMapping("/{courseId}/video/{videoId}")
    public Result getStructure(@RequestHeader(value="Authorization", required = false) String token, @PathVariable Integer courseId, @PathVariable Integer videoId) {
        return courseVideoService.findVideoByVideoId(videoId, token);
    }

    @PostMapping("/{courseId}/videoList")
    public Result getVideoList(@RequestHeader(value="Authorization", required = false) String token, @PathVariable Integer courseId) {
        return courseVideoService.getVideoList(courseId, token);
    }

    @PostMapping("/{courseId}/video")
    public Result getRoot(@RequestHeader(value="Authorization", required = false) String token, @PathVariable Integer courseId) {
        return courseVideoService.getRootVideo(courseId, token);
    }
}
