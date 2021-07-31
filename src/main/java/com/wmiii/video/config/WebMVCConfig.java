package com.wmiii.video.config;

import com.wmiii.video.handler.StudentLoginInterceptor;
import com.wmiii.video.handler.TeacherLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private StudentLoginInterceptor studentLoginInterceptor;

    @Autowired
    private TeacherLoginInterceptor teacherLoginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> studentPatterns = new ArrayList<>();
        List<String> teacherPatterns = new ArrayList<>();
        studentPatterns.add("/student/user_center");
        registry.addInterceptor(studentLoginInterceptor).addPathPatterns(studentPatterns);

        teacherPatterns.add("/teacher/user_center");
        teacherPatterns.add("/courses/create_courses");
        registry.addInterceptor(teacherLoginInterceptor).addPathPatterns(teacherPatterns);
    }
}
