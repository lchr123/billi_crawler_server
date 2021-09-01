package com.example.demo.services;

import org.apache.http.HttpException;

import java.io.IOException;

public interface HttpHandleServiceI {
    void updateVideo(String type) throws HttpException, IOException;
}
