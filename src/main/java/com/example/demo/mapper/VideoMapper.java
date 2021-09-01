package com.example.demo.mapper;

import com.example.demo.model.Video;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VideoMapper {
    void insertVideo(Video stock);
    List<Video> getVideoByDate(String date, String type);
    List<Video> getVideoByAuthor(String authorId);
}
