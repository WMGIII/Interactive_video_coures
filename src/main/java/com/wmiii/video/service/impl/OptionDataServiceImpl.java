package com.wmiii.video.service.impl;

import com.wmiii.video.entity.OptionData;
import com.wmiii.video.entity.Student;
import com.wmiii.video.mapper.OptionDataMapper;
import com.wmiii.video.params.CourseVideoIdParam;
import com.wmiii.video.params.CourseVideoParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.service.OptionDataService;
import com.wmiii.video.service.StudentLoginService;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public class OptionDataServiceImpl implements OptionDataService {
    @Autowired
    private StudentLoginService studentLoginService;

    @Autowired
    private OptionDataMapper optionDataMapper;

    @Override
    public Result makeChoice(String token, Integer courseId, Integer videoId) {
        Student student;
        try {
            student = studentLoginService.checkToken(token);
        } catch(SignatureException s) {
            // System.out.println(s);
            return Result.fail(0, null);
        }
        OptionData optionData = new OptionData();
        optionData.setStudentId(student.getStudentId());
        optionData.setCourseId(courseId);
        optionData.setVideoId(videoId);
        optionData.setTime(System.currentTimeMillis() / 1000);  // 单位转换成秒
        if (storeChoice(optionData) != 0) {
            return Result.success(null);
        } else {
            return Result.fail(0, null);
        }
    }

    public Integer storeChoice(OptionData optionData) {
        return optionDataMapper.insert(optionData);
    }

    @Override
    public Result getChoiceData(String token, CourseVideoIdParam param) {
        return Result.success(optionDataMapper.getChoiceData(param.getCourseId(), param.getVideoId()));
    }
}
