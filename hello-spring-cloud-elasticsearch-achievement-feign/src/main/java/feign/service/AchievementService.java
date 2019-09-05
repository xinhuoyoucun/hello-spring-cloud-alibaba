package feign.service;

import feign.service.fall.SearchFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author by laiyuan
 * @Date 2019/8/14 14:12
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient(value = "nacos-elasticsearch-achievement",fallback = SearchFallBack.class)
public interface AchievementService {

    @GetMapping(value = "/suggest")
    List<String> suggest(@RequestParam(value ="text") String text);
}