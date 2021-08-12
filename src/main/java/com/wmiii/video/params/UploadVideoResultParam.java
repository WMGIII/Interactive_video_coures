package com.wmiii.video.params;

import lombok.Data;

@Data
public class UploadVideoResultParam {
    String videoName;
    String message;
    Integer videoId;
    String url;
}
