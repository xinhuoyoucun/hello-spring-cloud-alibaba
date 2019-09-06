package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service;

import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.vo.ProjectVO;

import java.util.List;
import java.util.Map;

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

    /**
     * 搜索
     * @param text
     * @return
     */
    Map<String,Object> search(String text, int index, int pageSize);

}
