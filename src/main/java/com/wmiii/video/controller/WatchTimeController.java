package com.wmiii.video.controller;

import com.wmiii.video.params.CourseVideoIdParam;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.WatchTimeParam;
import com.wmiii.video.service.WatchTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class WatchTimeController {
    @Autowired
    private WatchTimeService watchTimeService;


    @PostMapping("/watch_time")
    public Result updateWatchTime(@RequestHeader(value="Authorization", required = false) String token, @RequestBody WatchTimeParam watchTimeParam) {
        System.out.println(watchTimeParam.getVideoId());
        System.out.println(watchTimeParam.getCourseId());
        System.out.println(watchTimeParam.getMTime());
        return watchTimeService.setTime(watchTimeParam, token);
    }

    @PostMapping("/watch_time/get")
    public Result getWatchTime(@RequestHeader(value="Authorization", required = false) String token, @RequestBody CourseVideoIdParam param) {
        return watchTimeService.getTimes(param);
    }
}
