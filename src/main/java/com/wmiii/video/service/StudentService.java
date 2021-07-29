package com.wmiii.video.service;

import com.wmiii.video.entity.Student;
import com.wmiii.video.params.Result;

public interface StudentService {

    Student findStudentByStudentId(Integer studentId);

    Student findStudentLogin(String email, String pwd);

    Result findUserByToken(String token);

    Student findStudentByEmail(String email);

    void save(Student student);

}
