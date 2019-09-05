package feign.service;

import com.laiyuan.hello.spring.cloud.alibaba.nacos.consumer.feign.service.fall.EchoServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author by laiyuan
 * @Date 2019/8/14 14:12
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient(value = "nacos-provider", fallback = EchoServiceFallback.class)
public interface EchoService {

    @GetMapping(value = "/echo/{message}")
    String echo(@PathVariable("message") String message);
}