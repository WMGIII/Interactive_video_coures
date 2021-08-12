package com.wmiii.video.params;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class UploadVideoParam {
    Integer videoId;
    Integer courseId;
    String name;
    String children;
    Boolean isRoot;
}
