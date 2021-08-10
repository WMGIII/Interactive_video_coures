package com.wmiii.video.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Component
public class QiniuUtils {

    public static  final String url = "https://qx0br5d5r.hn-bkt.clouddn.com/";

    @Value("${qiniu.accessKey}")
    private  String accessKey;
    @Value("${qiniu.accessSecretKey}")
    private  String accessSecretKey;

    public FileRecorder fileRecorder;

    public  boolean upload(MultipartFile file,String fileName){

        Configuration cfg = new Configuration(Region.huanan());
        String bucket = "interactive-video-course";
        Auth auth = Auth.create(accessKey, accessSecretKey);
        String upToken = auth.uploadToken(bucket);

        String key = null;

        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            fileRecorder = new FileRecorder(localTempDir);

            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            byte[] uploadBytes = file.getBytes();
            try {
                Response response = uploadManager.put(uploadBytes, fileName, upToken);
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }


            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

