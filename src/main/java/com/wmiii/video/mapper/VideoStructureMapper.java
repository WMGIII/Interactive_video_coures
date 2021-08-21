package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.CourseStructure;
import com.wmiii.video.entity.VideoStructure;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoStructureMapper extends BaseMapper<VideoStructure> {
    Integer updateStructure(Integer videoId, String children, String name);
}
