package com.wmiii.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmiii.video.entity.VideoStructure;
import com.wmiii.video.mapper.VideoStructureMapper;
import com.wmiii.video.params.CourseVideoParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.VideoStructureParam;
import com.wmiii.video.service.VideoStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public class VideoStructureServiceImpl implements VideoStructureService {
    @Autowired
    VideoStructureMapper videoStructureMapper;

    @Override
    public Result getStructure(Integer courseId) {
        LambdaQueryWrapper<VideoStructure> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoStructure::getCourseId, courseId);
        // queryWrapper.last("limit 1");
        return Result.success(videoStructureMapper.selectList(queryWrapper));
    }

    @Override
    public Result storeStructure(VideoStructureParam videoStructureParam) {
        VideoStructure videoStructure = new VideoStructure();
        videoStructure.setCourseId(videoStructureParam.getCourseId());
        videoStructure.setEdge(videoStructureParam.getEdge());
        videoStructure.setTitle(videoStructureParam.getTitle());
        videoStructureMapper.insert(videoStructure);
        return Result.success(null);
    }

    @Override
    public Result deleteStructure(CourseVideoParam courseVideoParam, String token) {
        return Result.success(videoStructureMapper.deleteStructure(courseVideoParam.getCourseId(), courseVideoParam.getTitle()));
    }
}
