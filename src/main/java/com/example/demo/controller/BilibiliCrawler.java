package com.example.demo.controller;

import com.example.demo.model.Video;
import com.example.demo.services.ServiceImpl.HttpHandleServiceImpl;
import com.example.demo.services.ServiceImpl.VideoServiceImpl;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("")
public class BilibiliCrawler {

    @Autowired
    VideoServiceImpl videoService;

    @Autowired
    HttpHandleServiceImpl httpHandleService;

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

//    @PostMapping("/video")
//    public String insertStock(@RequestBody Video stock) {
//        return videoService.insertVideo(stock);
//    }

    @GetMapping("/Video/byDate")
    public List<Video> getVideoByDate(@RequestParam String date, @RequestParam String type) { // date form "yyyy-mm-dd"
        return videoService.getVideoByDate(date, type);
    }

    @GetMapping("/Video/byAuthor")
    public List<Video> getVideoByAuthor(@RequestParam String authorId) { // date form "yyyy-mm-dd"
        return videoService.getVideoByAuthor(authorId);
    }

    // 每日批量导入video
    @PostMapping("/Video")
    public void updateVideo(String type) throws HttpException, IOException {
        httpHandleService.updateVideo(type);
        return;
    }
}
