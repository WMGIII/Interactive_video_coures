package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.CourseVideo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CourseVideoMapper extends BaseMapper<CourseVideo> {
    Integer insertVideo(CourseVideo courseVideo);

    Integer updateStructure(Integer videoId, String children, String name, Boolean isRoot);

    Integer setUrl(Integer courseId, String url);
}
