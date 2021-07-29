package com.wmiii.video.controller;

import com.wmiii.video.params.LoginParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.StudentLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_login")
public class StudentLoginController {
    @Autowired
    private StudentLoginService studentLoginService;

    @PostMapping
    public Result studentLogin(@RequestBody LoginParam loginParam) {
        return studentLoginService.studentLogin(loginParam);
    }
}
