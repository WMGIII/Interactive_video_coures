package com.wmiii.video.service;

import com.wmiii.video.entity.Student;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.Result;

public interface TeacherService {

    Teacher findTeacherByTeacherId(Integer teacherId);

    // 登录时查询
    Teacher findTeacherLogin(String email, String pwd);

    // Token验证
    Result findUserByToken(String token);

    Teacher findTeacherByEmail(String email);

    void save(Teacher teacher);

}
