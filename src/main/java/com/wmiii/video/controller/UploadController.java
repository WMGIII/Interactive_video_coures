package com.wmiii.video.controller;

import com.wmiii.video.entity.Course;
import com.wmiii.video.entity.CourseVideo;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.UploadVideoResultParam;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.CourseVideoService;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private TeacherLoginService teacherLoginService;

    @Qualifier("CourseService")
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoService courseVideoService;

    @Value("${qiniu.url}")
    private String qiniuUrl;

    @PostMapping("/video")
    public Result uploadVideo(@RequestHeader(value="Authorization", required = false) String token, @RequestParam("file")MultipartFile[] files, @RequestParam Integer courseId) {
        Teacher teacher = teacherLoginService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        Course course = (Course) courseService.findCourseById(courseId).getData();
        if (course == null) {
            return Result.fail(ErrorCode.COURSE_NOT_EXIST.getCode(), ErrorCode.COURSE_NOT_EXIST.getMsg());
        }

        if (course.getTeacherId() != teacher.getTeacherId()) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        List<UploadVideoResultParam> list = new ArrayList<>();
        UploadVideoResultParam uploadVideoResultParam;
        for (MultipartFile file: files) {
            uploadVideoResultParam = new UploadVideoResultParam();
            String videoName = file.getOriginalFilename();
            uploadVideoResultParam.setVideoName(videoName);
            if (courseVideoService.findVideoByVideoName(videoName, courseId) != null) {
                uploadVideoResultParam.setMessage("视频名称重复");
                uploadVideoResultParam.setVideoId(null);
                uploadVideoResultParam.setUrl(null);
                list.add(uploadVideoResultParam);
                continue;
            }
            String fileType=videoName.substring(videoName.lastIndexOf("."), videoName.length());
            Integer videoId = courseVideoService.storeVideo(videoName, courseId, teacher.getTeacherId(), fileType, false);

            if (qiniuUtils.upload(file, videoId.toString() + fileType)) {
                // String fileType=videoName.substring(videoName.lastIndexOf("."), videoName.length());
                uploadVideoResultParam.setMessage("success");
                uploadVideoResultParam.setVideoId(videoId);
                uploadVideoResultParam.setUrl(qiniuUrl + videoId + fileType);
                list.add(uploadVideoResultParam);
                // courseVideoService.setUrl(videoId, fileType);
            } else {
                courseVideoService.deleteByVideoId(videoId);
                uploadVideoResultParam.setMessage("视频上传失败");
                uploadVideoResultParam.setVideoId(null);
                uploadVideoResultParam.setUrl(null);
                list.add(uploadVideoResultParam);
            }
        }
        return Result.success(list);
    }
}
