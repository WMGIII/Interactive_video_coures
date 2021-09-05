package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.OptionData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionDataMapper extends BaseMapper<OptionData> {
    Integer getChoiceData(Integer courseId, Integer videoId);
}
