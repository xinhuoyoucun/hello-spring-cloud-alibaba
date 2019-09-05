package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service;

import java.util.List;

/**
 * @author by laiyuan
 * @Date 2019/9/4 14:16
 * @Description: TODO
 * @Version 1.0
 */
public interface AchievementService {
    /**
     * 自动完成建议
     * @param text
     * @return
     */
    List<String> suggest(String text);
}
