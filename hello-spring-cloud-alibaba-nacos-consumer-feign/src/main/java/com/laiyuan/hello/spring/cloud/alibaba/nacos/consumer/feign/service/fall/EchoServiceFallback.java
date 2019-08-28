package com.laiyuan.hello.spring.cloud.alibaba.nacos.consumer.feign.service.fall;

import com.laiyuan.hello.spring.cloud.alibaba.nacos.consumer.feign.service.EchoService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author by laiyuan
 * @Date 2019/8/14 14:55
 * @Description: TODO
 * @Version 1.0
 */
@Component
public class EchoServiceFallback implements EchoService {

    @Override
    public String echo(@PathVariable("message") String message) {
        return "echo fallback:"+message;
    }
}
