package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.CourseVideo;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseVideoMapper extends BaseMapper<CourseVideo> {
    Integer insertVideo(CourseVideo courseVideo);

    Integer updateStructure(Integer videoId, String children, String name, Boolean isRoot);

    Integer setUrl(Integer courseId, String url);
}
