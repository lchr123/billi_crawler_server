package com.example.demo.services.ServiceImpl;

import com.example.demo.model.Video;
import com.example.demo.services.HttpHandleServiceI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class HttpHandleServiceImpl implements HttpHandleServiceI {

    @Autowired
    VideoServiceImpl videoService;

    private int rank = 0;

    public void updateVideo(String type) throws HttpException, IOException {

        // 如果今天已经录入，则跳过
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        if (videoService.getVideoByDate(ft.format(new Date()), type).size() != 0) return;
        // 限定几种类型
        List<String> types = new ArrayList<>();
        types.add("bangumi");types.add("documentary");types.add("music");types.add("dance");types.add("game");types.add("knowledge");
        types.add("tech");types.add("life");types.add("food");types.add("kichiku");types.add("animal");types.add("fashion");types.add("movie");types.add("tv");
        if (!types.contains(type)) return;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpGet request = new HttpGet("https://www.bilibili.com/v/popular/rank/" + type);
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36 Edg/92.0.902.55");
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 200
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");

                //handle html
                Document document = Jsoup.parse(html);
                Elements elements = document.getElementsByClass("rank-list-wrap").get(0).getElementsByClass("content");
                // pick top100 videos
                List <String> l = elements.stream().limit(100).map(element -> {
                    Video video = handleElement(element,type);
                    videoService.insertVideo(video);
                    return "insert ok!";
                }).collect(Collectors.toList());
                rank = 0;
            } else {
                System.out.println(response.getStatusLine().getStatusCode() + "not 200!");
            }
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            HttpClientUtils.closeQuietly(httpClient);
            HttpClientUtils.closeQuietly(response);
        }


        return;
    }
    public Video handleElement(Element element, String type) {
        Video video = new Video();
        video.setId(UUID.randomUUID().toString());
        video.setRank(++rank);
        video.setType(type);
        if (type.equals("tv") || type.equals("documentary") || type.equals("bangumi") || type.equals("movie")) { // 这几类没有up主
            video.setAuthor_id("bilibili");
            video.setAuthor_name("bilibili");
        }
        else {
            video.setAuthor_id(element.getElementsByClass("detail").toString().split("(\\.com\\/)|(\"\\>\\<span)")[1]);
            video.setAuthor_name(element.getElementsByClass("data-box up-name").toString().split("(\\<\\/i\\> )|( \\<\\/span)")[1]);
        }
        video.setVideo_url(element.getElementsByClass("title").toString().split("(\\/\\/)|(\" target)")[1]);
        video.setVideo_name(element.getElementsByClass("title").toString().split("(title\"\\>)|(\\<\\/a)")[1]);
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        video.setDate(ft.format(new Date()));
        video.setPlay_num(element.getElementsByClass("data-box").get(0).toString().split("(\\<\\/i\\> )|( \\<\\/span)")[1]);
        video.setComment_num(element.getElementsByClass("data-box").get(1).toString().split("(\\<\\/i\\> )|( \\<\\/span)")[1]);
        video.getAuthor_id();
        return video;
    }
}
