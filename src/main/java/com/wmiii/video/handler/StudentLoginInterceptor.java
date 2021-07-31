package com.wmiii.video.handler;

import com.alibaba.fastjson.JSON;
import com.wmiii.video.entity.Student;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.StudentLoginService;
import com.wmiii.video.utils.StudentThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class StudentLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StudentLoginService studentLoginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg())));
            return false;
        }

        Student student = studentLoginService.checkToken(token);
        if (student == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg())));
            return false;
        }

        StudentThreadLocal.put(student);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StudentThreadLocal.remove();
    }
}
