package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.controller;

import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service.AchievementService;
import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/search")
    public Map<String,Object> search(@RequestParam("text") String text, @RequestParam("index") int index, @RequestParam("pageSize") int pageSize) {
        Map<String,Object> projects = achievementService.search(text,index,pageSize);
        return projects;
    }

}
