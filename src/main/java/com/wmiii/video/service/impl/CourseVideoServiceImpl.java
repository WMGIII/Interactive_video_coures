package com.wmiii.video.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmiii.video.entity.*;
import com.wmiii.video.mapper.VideoStructureMapper;
import com.wmiii.video.mapper.CourseVideoMapper;
import com.wmiii.video.params.*;
import com.wmiii.video.service.*;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private VideoStructureMapper videoStructureMapper;

    @Value("${qiniu.url}")
    private String qiniuUrl;

    @Override
    public Result findVideoByVideoId(Integer videoId, String token) {
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
            return Result.success(findVideoById(videoId));
        }

        return Result.success(findVideoById(videoId));
    }

    public CourseVideo findVideoById(Integer videoId) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseVideo::getVideoId, videoId);
        queryWrapper.last("limit 1");

        return courseVideoMapper.selectOne(queryWrapper);
    }

    @Override
    public Result submit(UploadVideoParam uploadVideoParam, String token) {
        Teacher teacher = teacherService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        Course course = (Course) courseService.findCourseById(uploadVideoParam.getCourseId()).getData();
        if (course == null) {
            return Result.fail(ErrorCode.COURSE_NOT_EXIST.getCode(), ErrorCode.COURSE_NOT_EXIST.getMsg());
        }
        if ((course.getTeacherId() != teacher.getTeacherId()) || (course.getCourseId() != uploadVideoParam.getCourseId())) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        // courseVideoMapper.updateStructure(uploadVideoParam.getVideoId(), JSON.toJSONString(uploadVideoParam.getChildren()), uploadVideoParam.getName(), uploadVideoParam.getIsRoot());
        CourseVideo courseVideo = findVideoById(uploadVideoParam.getVideoId());
        courseVideo.setChildren(JSON.toJSONString(uploadVideoParam.getChildren()));
        courseVideo.setName(uploadVideoParam.getName());
        courseVideo.setIsRoot(uploadVideoParam.getIsRoot());

        return updateVideo(courseVideo);
    }

    public Result updateVideo(CourseVideo courseVideo) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseVideo::getVideoId, courseVideo.getVideoId());
        courseVideoMapper.update(courseVideo, queryWrapper);
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
    public Integer storeVideo(String videoName, Integer courseId, Integer teacherId, String fileType, Boolean isRoot) {
        CourseVideo courseVideo = new CourseVideo();
        courseVideo.setName(courseId + "/" + videoName);
        courseVideo.setCourseId(courseId);
        courseVideo.setTeacherId(teacherId);
        courseVideo.setFileType(fileType);
        courseVideo.setIsRoot(true);
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
        queryWrapper.eq(CourseVideo::getName, videoName);
        queryWrapper.last("limit 1");

        return courseVideoMapper.selectOne(queryWrapper).getVideoId();
    }

    @Override
    public CourseVideo findVideoByVideoName(String videoName, Integer courseId) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(CourseVideo::getCourseId, courseId);
        queryWrapper.eq(CourseVideo::getName, courseId + "/" + videoName);
        queryWrapper.last("limit 1");
        return courseVideoMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteByVideoId(Integer videoId) {
        return courseVideoMapper.deleteById(videoId);
    }

    @Override
    public Boolean setBlankStructure(Integer videoId, Integer courseId, String fileType) {
        CourseStructure structure = new CourseStructure();
        structure.setCourseId(courseId);
        structure.setJsonStructure("");
        structure.setLastChange(System.currentTimeMillis());
        if (this.videoStructureMapper.insert(structure) != 0) {
            return true;
        }
        return false;
    }

    /*
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

        return videoStructureMapper.selectOne(queryWrapper).getJsonStructure();
    }

    public List<CourseVideo> getStructureList(String jsonStructure) {
        return JSON.parseArray(jsonStructure, CourseVideo.class);
    }*/

    @Override
    public Result getVideoList(Integer courseId, String token) {
        Teacher teacher = teacherService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        if (((Course) courseService.findCourseById(courseId).getData()).getTeacherId() != teacher.getTeacherId()) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        return Result.success(findVideoByCourseId(courseId));
    }

    public List<CourseVideo> findVideoByCourseId(Integer courseId) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseVideo::getCourseId, courseId);

        List<CourseVideo> list = courseVideoMapper.selectList(queryWrapper);
        for (CourseVideo video: list) {
            video.setFileType(qiniuUrl + video.getVideoId() + video.getFileType());
        }
        return list;
    }

    @Override
    public Integer setUrl(Integer videoId, String fileType) {
        CourseVideo courseVideo = findVideoById(videoId);
        courseVideo.setFileType(fileType);
        courseVideo.setUrl(qiniuUrl + videoId + fileType);
        updateVideo(courseVideo);
        return 1;
    }

    @Override
    public Result getRootVideo(Integer courseId, String token) {
        CourseVideo courseVideo = findRootVideo(courseId);
        if (courseVideo == null) {
            return Result.fail(ErrorCode.COURSE_NOT_EXIST.getCode(), ErrorCode.COURSE_NOT_EXIST.getMsg());
        }
        return Result.success(courseVideo);
    }

    public CourseVideo findRootVideo(Integer courseId) {
        LambdaQueryWrapper<CourseVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseVideo::getCourseId, courseId);
        queryWrapper.eq(CourseVideo::getIsRoot, 1);
        queryWrapper.last("limit 1");

        return courseVideoMapper.selectOne(queryWrapper);
    }
}
