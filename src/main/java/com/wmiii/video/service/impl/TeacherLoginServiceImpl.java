package com.wmiii.video.service.impl;

import com.alibaba.fastjson.JSON;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.service.TeacherService;
import com.wmiii.video.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TeacherLoginServiceImpl implements TeacherLoginService {
    @Autowired @Lazy
    private TeacherService teacherService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "678^&*";

    @Override
    public Result teacherLogin(LoginParam loginParam) {
        String email = loginParam.getEmail();
        String pwd = loginParam.getPwd();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg()) ;
        }

        pwd = DigestUtils.md5Hex(pwd + salt);
        Teacher teacher = teacherService.findTeacherLogin(email, pwd);
        if (teacher == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(teacher.getTeacherId(), "teacher");
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(teacher), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public Teacher checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token, "teacher");
        if (stringObjectMap == null) {
            return null;
        }
        String teacherJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(teacherJSON)) {
            return null;
        }
        Teacher teacher = JSON.parseObject(teacherJSON, Teacher.class);
        if (teacher.getTeacherId() == null || teacher.getTeacherName() == null) {
            return null;
        }
        return teacher;
    }

    @Override
    public Result register(LoginParam loginParam) {
        String email = loginParam.getEmail();
        String pwd = loginParam.getPwd();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        Teacher teacher = teacherService.findTeacherByEmail(email);
        if (teacher != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        teacher = new Teacher();
        teacher.setEmail(email);
        teacher.setPwd(DigestUtils.md5Hex(pwd + salt));
        teacher.setTeacherName(email);
        teacher.setTeacherIntro("");
        teacher.setCreateDate(System.currentTimeMillis());
        teacher.setLastLogin(System.currentTimeMillis());

        this.teacherService.save(teacher);

        String token = JWTUtils.createToken(teacher.getTeacherId(), "teacher");
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(teacher), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
