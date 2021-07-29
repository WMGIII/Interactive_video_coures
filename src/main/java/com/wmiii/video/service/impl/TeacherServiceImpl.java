package com.wmiii.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmiii.video.entity.Teacher;

import com.wmiii.video.mapper.TeacherMapper;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.vo.LoginTeacherVo;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TeacherLoginService teacherLoginService;

    @Override
    public Teacher findTeacherByTeacherId(Integer teacherId) {
        return teacherMapper.selectById(teacherId);
    }

    @Override
    public Teacher findTeacherLogin(String email, String pwd) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getEmail, email);
        queryWrapper.eq(Teacher::getPwd, pwd);
        queryWrapper.select(Teacher::getEmail, Teacher::getTeacherId, Teacher::getTeacherName, Teacher::getTeacherIntro);
        queryWrapper.last("limit 1");

        return teacherMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        Teacher teacher = teacherLoginService.checkToken(token);
        if (teacher == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginTeacherVo loginTeacherVo = new LoginTeacherVo();
        loginTeacherVo.setTeacherId(teacher.getTeacherId());
        loginTeacherVo.setTeacherName(teacher.getTeacherName());
        loginTeacherVo.setEmail(teacher.getEmail());
        loginTeacherVo.setTeacherIntro(teacher.getTeacherIntro());
        return Result.success(loginTeacherVo);
    }

    @Override
    public Teacher findTeacherByEmail(String email) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getEmail, email);
        queryWrapper.last("limit 1");
        return this.teacherMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(Teacher teacher) {
        this.teacherMapper.insert(teacher);
    }
}
