package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.controller;

import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by laiyuan
 * @Date 2019/8/28 15:35
 * @Description: TODO
 * @Version 1.0
 */
@RestController
public class AchievementController {

   @Autowired
   private AchievementService achievementService;


    @GetMapping(value = "/suggest")
    public List<String> suggest(String text) {
        return achievementService.suggest(text);
    }

}
