package com.wmiii.video.service.impl;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Error;
import com.wmiii.video.entity.Course;
import com.wmiii.video.entity.CourseVideo;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.mapper.CourseVideoMapper;
import com.wmiii.video.params.*;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.CourseVideoService;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Service
@Transactional
public class CourseVideoServiceImpl implements CourseVideoService {
    @Autowired
    private TeacherLoginService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoMapper courseVideoMapper;

    @Override
    public Result findVideoByVideoId(Integer videoId) {
        return null;
    }

    @Override
    public Result submit(UploadCourseParam uploadCourseParam, String token) {
        Teacher teacher = teacherService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        Course course = (Course) courseService.findCourseById(uploadCourseParam.getCourseId()).getData();
        if (course == null) {
            return Result.fail(ErrorCode.COURSE_NOT_EXIST.getCode(), ErrorCode.COURSE_NOT_EXIST.getMsg());
        }
        if (course.getTeacherId() != teacher.getTeacherId()) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        Integer result = storeVideos(uploadCourseParam, teacher.getTeacherId());
        if (result != 0) {
            return Result.success(result);
        } else {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
    }

    public Integer storeVideos(UploadCourseParam uploadCourseParam, Integer teacherId) {
        for (UploadVideoParam i: uploadCourseParam.getVideos()) {
            if (i.getOptionInfo() == null) {
                return getTree(uploadCourseParam, i,teacherId, null);
            }
        }
        return 0;
    }

    public Integer getTree(UploadCourseParam uploadCourseParam, UploadVideoParam uploadVideoParam, Integer teacherId, Integer optionId) {
        List<Integer> nextList = new ArrayList<>();
        for (int i = 0; i < uploadVideoParam.getOptionList().size(); i++) {
            for (UploadVideoParam v: uploadCourseParam.getVideos()) {
                if (v.getVideoName() == uploadVideoParam.getOptionList().get(i).getVideoName()) {
                    nextList.add(getTree(uploadCourseParam, v, teacherId, i));
                    break;
                }
            }
        }
        return storeVideo(uploadVideoParam, uploadCourseParam.getCourseId(), teacherId, optionId, nextList);
    }

    public Integer storeVideo(UploadVideoParam uploadVideoParam, Integer courseId, Integer teacherId, Integer optionId, List<Integer> nextList) {
        CourseVideo courseVideo = new CourseVideo();
        courseVideo.setCourseId(courseId);
        courseVideo.setTeacherId(teacherId);
        courseVideo.setVideoName(uploadVideoParam.getVideoName());
        courseVideo.setVideoIntro(uploadVideoParam.getVideoIntro());
        courseVideo.setOptionInfo(uploadVideoParam.getOptionInfo());
        courseVideo.setOptionId(optionId);
        courseVideo.setNextVideo(JSON.toJSONString(nextList));
        this.courseVideoMapper.insert(courseVideo);

        // System.out.println(courseVideo.getVideoId());
        return courseVideo.getVideoId();
    }

}
