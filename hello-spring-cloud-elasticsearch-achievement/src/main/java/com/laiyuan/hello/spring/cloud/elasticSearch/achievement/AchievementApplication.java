package com.laiyuan.hello.spring.cloud.elasticSearch.achievement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: 
 * @Author: laiyuan
 * @Date: 2019/9/4 10:13
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AchievementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AchievementApplication.class, args);
    }

}
