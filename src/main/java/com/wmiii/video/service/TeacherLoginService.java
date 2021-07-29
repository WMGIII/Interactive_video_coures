package com.wmiii.video.service;

import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;

public interface TeacherLoginService {
    Result teacherLogin(LoginParam loginParam);

    Teacher checkToken(String token);

    Result register(LoginParam loginParam);
}
