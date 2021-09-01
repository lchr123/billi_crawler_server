package com.example.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class Video {
    private String id;
    private String video_url;
    private String video_name;
    private String type;
    private String author_id;
    private String author_name;
    private String play_num;
    private String comment_num;
    private String date;
    private int rank;
}
