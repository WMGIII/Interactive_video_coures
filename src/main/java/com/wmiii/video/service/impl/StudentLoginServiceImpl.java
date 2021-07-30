package com.wmiii.video.service.impl;

import com.alibaba.fastjson.JSON;
import com.wmiii.video.entity.Student;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.StudentLoginService;
import com.wmiii.video.service.StudentService;
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
public class StudentLoginServiceImpl implements StudentLoginService {
    @Autowired @Lazy
    private StudentService studentService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "123!@#";

    @Override
    public Result studentLogin(LoginParam loginParam) {
        String email = loginParam.getEmail();
        String pwd = loginParam.getPwd();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg()) ;
        }

        pwd = DigestUtils.md5Hex(pwd + salt);
        Student student = studentService.findStudentLogin(email, pwd);
        if (student == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(student.getStudentId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(student), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public Student checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            return null;
        }
        String studentJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(studentJSON)) {
            return null;
        }
        return(JSON.parseObject(studentJSON, Student.class));
    }

    @Override
    public Result register(LoginParam loginParam) {
        String email = loginParam.getEmail();
        String pwd = loginParam.getPwd();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        Student student = studentService.findStudentByEmail(email);
        if (student != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        student = new Student();
        student.setEmail(email);
        student.setPwd(DigestUtils.md5Hex(pwd + salt));
        student.setStudentName(email);
        student.setStudentNumber("");
        student.setCreateDate(System.currentTimeMillis());
        student.setLastLogin(System.currentTimeMillis());

        this.studentService.save(student);

        String token = JWTUtils.createToken(student.getStudentId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(student), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
