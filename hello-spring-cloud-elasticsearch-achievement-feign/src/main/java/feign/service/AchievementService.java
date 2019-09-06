package feign.service;

import feign.service.fall.SearchFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * @author by laiyuan
 * @Date 2019/8/14 14:12
 * @Description: 搜索消费者
 * @Version 1.0
 */
@FeignClient(value = "nacos-elasticsearch-achievement",fallback = SearchFallBack.class)
public interface AchievementService {

    /**
     * 自动完成
     * @param text 文本
     * @return 关键词数组
     */
    @GetMapping(value = "/suggest")
    List<String> suggest(@RequestParam(value ="text") String text);

    /**
     * 搜索
     * @param text 文本
     * @param index from
     * @param pageSize size
     * @return 项目数组和总数
     */
    @GetMapping(value = "/search")
    Map<String,Object> search(@RequestParam("text") String text, @RequestParam("index") int index, @RequestParam("pageSize") int pageSize);

}