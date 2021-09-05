package com.wmiii.video;

import com.wmiii.video.params.WatchTimeParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.alibaba.fastjson.JSON;

@SpringBootTest
class VideoApplicationTests {

    @Test
    void contextLoads() {
        WatchTimeParam param = new WatchTimeParam();
        param.setVideoId(1);
        param.setCourseId(5);
        param.setMTime(3);
        System.out.println(JSON.toJSON(param));
    }

}
