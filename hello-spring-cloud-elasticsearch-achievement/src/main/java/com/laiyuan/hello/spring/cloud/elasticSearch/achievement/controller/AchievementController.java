package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.controller;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;

/**
 * @author by laiyuan
 * @Date 2019/8/28 15:35
 * @Description: TODO
 * @Version 1.0
 */
public class AchievementController {

    public static void main(String[] args) {
        final String HOSTNAME = "192.168.0.188";


        //初始化连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOSTNAME, 9200, "http"),
                        new HttpHost(HOSTNAME, 9201, "http")));

        GetRequest request = new GetRequest(
                "test",
                "1");


        try {
            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
            String index = getResponse.getIndex();
            String id = getResponse.getId();
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                String about = sourceAsMap.get("about").toString();
                System.out.println(about);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //关闭连接
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("关闭失败");
            e.printStackTrace();
        }
    }
}
