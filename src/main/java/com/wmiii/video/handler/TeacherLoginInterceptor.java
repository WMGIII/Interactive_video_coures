package com.wmiii.video.handler;

import com.alibaba.fastjson.JSON;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.TeacherLoginService;
import com.wmiii.video.utils.TeacherThreadLocal;
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
public class TeacherLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TeacherLoginService teacherLoginService;

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

        Teacher teacher = teacherLoginService.checkToken(token);
        if (teacher == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg())));
            return false;
        }

        TeacherThreadLocal.put(teacher);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TeacherThreadLocal.remove();
    }
}
