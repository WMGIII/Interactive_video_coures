package com.wmiii.video.utils;

import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TencentUtils {
    @Value("${tencent.secretId}")
    private String secretId;
    @Value("${tencent.secretKey}")
    private String secretKey;

    public VodUploadClient client = new VodUploadClient(secretId, secretKey);

    public void uploadVideo(MultipartFile file, String fileName) {
        VodUploadRequest request = new VodUploadRequest();

    }
}
