package com.wmiii.video.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiniu.http.Error;
import com.wmiii.video.entity.*;
import com.wmiii.video.mapper.CourseStructureMapper;
import com.wmiii.video.mapper.CourseVideoMapper;
import com.wmiii.video.params.*;
import com.wmiii.video.service.*;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private StudentLoginService studentLoginService;

    @Autowired @Lazy
    private CourseService courseService;

    @Autowired
    private CourseVideoMapper courseVideoMapper;

    @Autowired
    private CourseStructureMapper courseStructureMapper;

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
        if ((course.getTeacherId() != teacher.getTeacherId()) || (course.getCourseId() != uploadCourseParam.getCourseId())) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        courseStructureMapper.updateStructure(course.getCourseId(), JSON.toJSONString(uploadCourseParam.getVideos()), System.currentTimeMillis());

        return Result.success(null);
    }

    /*
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
    }*/

    @Override
    public Integer storeVideo(String videoName, Integer courseId, Integer teacherId) {
        CourseVideo courseVideo = new CourseVideo();
        courseVideo.setVideoName(courseId + "/" + videoName);
        courseVideo.setCourseId(courseId);
        courseVideo.setTeacherId(teacherId);
        // courseVideo.setVideoName(uploadVideoParam.getVideoName());
        // courseVideo.setVideoIntro(uploadVideoParam.getVideoIntro());
        this.courseVideoMapper.insert(courseVideo);

        // System.out.println(courseVideo.getVideoId());
        return courseVideo.getVideoId();
    }

    @Override
    public Integer getVideoIdByOriginName(Integer courseId, String videoName) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseVideo::getCourseId, courseId);
        queryWrapper.eq(CourseVideo::getVideoName, videoName);
        queryWrapper.last("limit 1");

        return courseVideoMapper.selectOne(queryWrapper).getVideoId();
    }

    @Override
    public CourseVideo findVideoByVideoName(String videoName, Integer courseId) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(CourseVideo::getCourseId, courseId);
        queryWrapper.eq(CourseVideo::getVideoName, courseId + "/" + videoName);
        queryWrapper.last("limit 1");
        return courseVideoMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteByVideoId(Integer videoId) {
        return courseVideoMapper.deleteById(videoId);
    }

    @Override
    public Boolean setBlankStructure(Integer courseId) {
        CourseStructure structure = new CourseStructure();
        structure.setCourseId(courseId);
        structure.setJsonStructure("");
        structure.setLastChange(System.currentTimeMillis());
        if (this.courseStructureMapper.insert(structure) != 0) {
            return true;
        }

        return false;
    }

    @Override
    public Result getStructureByCourseId(Integer courseId, String token) {
        Teacher teacher;
        Student student = new Student();
        try {
            student = studentLoginService.checkToken(token);
        } catch(SignatureException s) {
            // System.out.println(s);
        }

        if (student == null) {
            teacher = teacherService.checkToken(token);
            if (teacher == null) {
                return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
            }

            if (((Course) courseService.findCourseById(courseId).getData()).getTeacherId() != teacher.getTeacherId()) {
                return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
            }

            return Result.success(getStructureList(getStructureJson(courseId)));
        }

        if (courseService.findStudentCourse(student.getStudentId(), courseId) == null) {
            return Result.fail(ErrorCode.NO_ENTRANCE.getCode(), ErrorCode.NO_ENTRANCE.getMsg());
        }

        return Result.success(getStructureList(getStructureJson(courseId)));
    }

    public String getStructureJson(Integer courseId) {
        LambdaQueryWrapper<CourseStructure> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseStructure::getCourseId, courseId);
        queryWrapper.last("limit 1");

        return courseStructureMapper.selectOne(queryWrapper).getJsonStructure();
    }

    public List<CourseVideo> getStructureList(String jsonStructure) {
        return JSON.parseArray(jsonStructure, CourseVideo.class);
    }
}
