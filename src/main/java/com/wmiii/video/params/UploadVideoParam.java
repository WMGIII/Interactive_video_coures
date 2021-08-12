package com.wmiii.video.params;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class UploadVideoParam {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    Integer videoId;
    Integer courseId;
    String name;
    String url;
    String children;
    Boolean isRoot;
}
