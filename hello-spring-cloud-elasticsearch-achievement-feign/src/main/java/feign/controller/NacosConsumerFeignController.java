package com.laiyuan.hello.spring.cloud.alibaba.nacos.consumer.feign.controller;

import com.laiyuan.hello.spring.cloud.alibaba.nacos.consumer.feign.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by laiyuan
 * @Date 2019/8/14 14:14
 * @Description: TODO
 * @Version 1.0
 */
@RestController
public class NacosConsumerFeignController {

    @Autowired
    private EchoService echoService;

    @GetMapping(value = "/echo/hi")
    public String echo() {
        return echoService.echo("Hi Feign");
    }
}
