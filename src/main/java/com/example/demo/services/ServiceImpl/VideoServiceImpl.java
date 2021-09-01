package com.example.demo.services.ServiceImpl;

import com.example.demo.mapper.VideoMapper;
import com.example.demo.model.Video;
import com.example.demo.services.VideoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoServiceI {

    @Autowired
    private VideoMapper kichikuVideoMapper;

    @Override
    public String insertVideo(Video video) {
        kichikuVideoMapper.insertVideo(video);
        return "succeed";
    }

    @Override
    public List<Video> getVideoByDate(String date, String type) {
        return kichikuVideoMapper.getVideoByDate(date, type);
    }

    @Override
    public List<Video> getVideoByAuthor(String authorId) {
        return kichikuVideoMapper.getVideoByAuthor(authorId);
    }
}
