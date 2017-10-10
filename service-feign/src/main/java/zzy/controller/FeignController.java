package zzy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zzy.service.FeignService;

/**
 * Created by zhaozhengyang on 2017/9/26.
 *
 * Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。
 * 使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。
 * Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。
 */
@RestController
public class FeignController {
    @Autowired
    private FeignService feignService;
    @RequestMapping(value = "/link",method = RequestMethod.GET)
    public String linkClient(@RequestParam String name){
        return feignService.linkClient(name);
    }
}
