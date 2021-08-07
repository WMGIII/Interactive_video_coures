package com.wmiii.video.controller;

import com.wmiii.video.entity.Course;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.CourseVideoService;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private TeacherLoginService teacherLoginService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoService courseVideoService;

    @PostMapping("/video")
    public Result uploadVideo(@RequestHeader(value="Authorization", required = false) String token, @RequestParam("video")MultipartFile file, @RequestParam Integer courseId) {
        Teacher teacher = teacherLoginService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        Course course = (Course) courseService.findCourseById(courseId).getData();
        if (course.getTeacherId() != teacher.getTeacherId()) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        String videoName = file.getOriginalFilename();
        Integer videoId = courseVideoService.getVideoIdByOriginName(course.getCourseId(), videoName);
        if (videoId == null) {
            return Result.fail(10010, "视频上传错误");
        }

        if (qiniuUtils.upload(file, videoId.toString())) {
            return Result.success(QiniuUtils.url + videoId);
        }
        return Result.fail(ErrorCode.VIDEO_UPLOAD_FAIL.getCode(), ErrorCode.VIDEO_UPLOAD_FAIL.getMsg());
    }
}
