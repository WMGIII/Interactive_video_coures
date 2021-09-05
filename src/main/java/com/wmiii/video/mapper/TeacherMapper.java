package com.wmiii.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wmiii.video.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    public Teacher getTeacherById(Integer id);
    // public List<Teacher> getAll();
}
