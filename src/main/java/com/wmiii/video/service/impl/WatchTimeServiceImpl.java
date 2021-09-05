package com.wmiii.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmiii.video.entity.Student;
import com.wmiii.video.entity.WatchTime;
import com.wmiii.video.mapper.WatchTimeMapper;
import com.wmiii.video.params.CourseVideoIdParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.WatchTimeParam;
import com.wmiii.video.service.StudentLoginService;
import com.wmiii.video.service.WatchTimeService;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public class WatchTimeServiceImpl implements WatchTimeService {
    @Autowired
    private WatchTimeMapper watchTimeMapper;

    @Autowired
    private StudentLoginService studentLoginService;

    @Override
    public Result setTime(WatchTimeParam watchTimeParam, String token) {
        Student student;
        try {
            student = studentLoginService.checkToken(token);
        } catch(SignatureException s) {
            // System.out.println(s);
            return Result.fail(0, null);
        }

        Integer i = watchTimeMapper.updateTime(student.getStudentId(), watchTimeParam.getCourseId(), watchTimeParam.getVideoId(), watchTimeParam.getMTime());
        // System.out.println(i);
        if (i != 0) {
            return Result.success("更新成功");
        } else {
            WatchTime watchTime = new WatchTime();
            watchTime.setStudentId(student.getStudentId());
            watchTime.setCourseId(watchTimeParam.getCourseId());
            watchTime.setVideoId(watchTimeParam.getVideoId());
            watchTime.setMTime(watchTimeParam.getMTime());
            watchTimeMapper.insert(watchTime);
            return Result.success("创建成功");
        }
    }

    public Integer setTime(WatchTimeParam watchTimeParam, Integer studentId) {
        WatchTime watchTime = new WatchTime();
        watchTime.setStudentId(studentId);
        watchTime.setCourseId(watchTimeParam.getCourseId());
        watchTime.setVideoId(watchTimeParam.getVideoId());
        watchTime.setMTime(watchTimeParam.getMTime());
        return watchTimeMapper.insert(watchTime);
    }

    @Override
    public Result getTimes(CourseVideoIdParam param) {
        LambdaQueryWrapper<WatchTime> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WatchTime::getCourseId, param.getCourseId());
        queryWrapper.eq(WatchTime::getVideoId, param.getVideoId());
        return Result.success(watchTimeMapper.selectList(queryWrapper));
    }
}
