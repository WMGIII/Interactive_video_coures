package com.wmiii.video.params;

import lombok.Data;

import java.util.List;

@Data
public class UploadVideoParam {
    Integer videoId;
    String videoName;
    String videoIntro;
    String optionInfo;
    List<UploadOptionParam> optionList;
}
