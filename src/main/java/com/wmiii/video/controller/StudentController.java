package com.wmiii.video.controller;

import com.wmiii.video.params.Result;
import com.wmiii.video.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/usercenter")
    public Result currentUser(@RequestHeader(value="Authorization", required = false) String token) {
        return studentService.findUserByToken(token);
    }
}
