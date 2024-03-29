package feign.controller;

import feign.common.Response;
import feign.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author by laiyuan
 * @Date 2019/8/14 14:14
 * @Description: TODO
 * @Version 1.0
 */
@RestController
public class NacosConsumerFeignController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping(value = "/suggest")
    public Response suggest(@RequestParam(value ="text") String text) {
        List<String> list = achievementService.suggest(text);
        return new Response().success(list);
    }


    @GetMapping(value = "/search")
    public Response search(@RequestParam("text") String text, @RequestParam("index") int index, @RequestParam("pageSize") int pageSize){
        Map<String,Object> map = achievementService.search(text,index,pageSize);
        return new Response().success(map);
    }

}
