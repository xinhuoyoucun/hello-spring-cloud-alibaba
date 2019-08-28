package com.laiyuan.hello.spring.cloud.alibaba.nacos.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by laiyuan
 * @Date 2019/8/14 13:31
 * @Description: TODO
 * @Version 1.0
 */
@RestController
public class EchoController {

    @Value("${server.port}")
    private String port;

    /**
     * 读取动态配置
     */
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @GetMapping(value = "/echo/{message}")
    public String echo(@PathVariable String message) {
        return "Hello Nacos Discovery " + message + ",from port:" + port;
    }

    @GetMapping(value = "hi")
    public String hi(){
        return "hi " + configurableApplicationContext.getEnvironment().getProperty("user.name");
    }
}
