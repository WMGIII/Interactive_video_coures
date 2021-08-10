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

import java.util.HashMap;
import java.util.Map;

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
    public Result uploadVideo(@RequestHeader(value="Authorization", required = false) String token, @RequestParam("video")MultipartFile[] files, @RequestParam Integer courseId) {
        Teacher teacher = teacherLoginService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        Course course = (Course) courseService.findCourseById(courseId).getData();
        if (course.getTeacherId() != teacher.getTeacherId()) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        Map<String, Object> resultMap = new HashMap<>();
        for (MultipartFile file: files) {
            String videoName = file.getOriginalFilename();
            if (courseVideoService.findVideoByVideoName(videoName, courseId) != null) {
                resultMap.put(videoName, "视频文件名重复");
                continue;
            }
            Integer videoId = courseVideoService.storeVideo(videoName, courseId, teacher.getTeacherId());
            if (qiniuUtils.upload(file, videoId.toString())) {
                resultMap.put(videoName, videoId);
            } else {
                courseVideoService.deleteByVideoId(videoId);
                resultMap.put(videoName, "视频上传失败");
            }
        }
        return Result.success(resultMap);
    }
}
