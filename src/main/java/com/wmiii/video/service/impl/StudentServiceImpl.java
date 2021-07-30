package com.wmiii.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmiii.video.entity.Student;
import com.wmiii.video.mapper.StudentMapper;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.vo.LoginStudentVo;
import com.wmiii.video.service.StudentLoginService;
import com.wmiii.video.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired @Lazy
    private StudentLoginService studentLoginService;

    @Override
    public Student findStudentByStudentId(Integer studentId) {
        return studentMapper.selectById(studentId);
    }

    @Override
    public Student findStudentLogin(String email, String pwd) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getEmail, email);
        queryWrapper.eq(Student::getPwd, pwd);
        queryWrapper.select(Student::getEmail, Student::getStudentId, Student::getStudentName, Student::getStudentNumber);
        queryWrapper.last("limit 1");

        return studentMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        Student student = studentLoginService.checkToken(token);
        if (student == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginStudentVo loginStudentVo = new LoginStudentVo();
        loginStudentVo.setStudentId(student.getStudentId());
        loginStudentVo.setStudentName(student.getStudentName());
        loginStudentVo.setEmail(student.getEmail());
        loginStudentVo.setStudentNumber(student.getStudentNumber());
        return Result.success(loginStudentVo);
    }

    @Override
    public Student findStudentByEmail(String email) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getEmail, email);
        queryWrapper.last("limit 1");
        return this.studentMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(Student student) {
        this.studentMapper.insert(student);
    }
}
