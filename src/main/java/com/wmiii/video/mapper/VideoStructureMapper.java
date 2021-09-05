package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.CourseStructure;
import com.wmiii.video.entity.VideoStructure;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface VideoStructureMapper extends BaseMapper<VideoStructure> {
    Integer updateStructure(Integer id, String edge, String title);

    Integer deleteStructure(Integer courseId, String title);
}
