package com.wmiii.video.service;

import com.wmiii.video.entity.Student;
import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;

public interface StudentLoginService {
    Result studentLogin(LoginParam loginParam);

    Student checkToken(String token);

    Result register(LoginParam loginParam);
}
