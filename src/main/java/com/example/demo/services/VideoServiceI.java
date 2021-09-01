package com.example.demo.services;

import com.example.demo.model.Video;

import java.util.List;

public interface VideoServiceI {
    String insertVideo(Video stock);
    List<Video> getVideoByDate(String date, String type);
    List<Video> getVideoByAuthor(String authorId);
}
