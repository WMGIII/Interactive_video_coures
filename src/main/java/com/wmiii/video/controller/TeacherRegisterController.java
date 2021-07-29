package com.wmiii.video.controller;

import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.StudentLoginService;
import com.wmiii.video.service.TeacherLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher_register")
public class TeacherRegisterController {

    @Autowired
    private TeacherLoginService teacherLoginService;

    @PostMapping
    public Result teacherRegister(@RequestBody LoginParam loginParam) {
        return teacherLoginService.register(loginParam);
    }
}
