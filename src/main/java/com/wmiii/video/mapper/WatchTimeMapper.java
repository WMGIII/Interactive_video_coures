package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.WatchTime;
import com.wmiii.video.params.vo.StudentTimeVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WatchTimeMapper extends BaseMapper<WatchTime> {
    Integer updateTime(Integer studentId, Integer courseId, Integer videoId, Integer mTime);

    StudentTimeVo getTimes(Integer courseId, Integer videoId);
}
