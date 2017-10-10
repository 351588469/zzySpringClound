package zzy.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhaozhengyang on 2017/9/26.
 * mod on 2017.10.9 添加断路器Hystrix功能
 */
@Service
public class ZzyService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "zzyError")//该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法
    /**
     * 当sercvice-ribbon通过restTemplate调用zzyClient的client接口时，
     * 因为用ribbon进行了负载均衡，会轮流的调用client,client2
     */
    public String linkClient(String name){
        /**
         * RestTemplate的getForObject完成get请求、postForObject完成post请求、put对应的完成put请求、delete完成delete请求；
         * 还有execute可以执行任何请求的方法，需要你设置RequestMethod来指定当前请求类型。
         */
        return restTemplate.getForObject("http://zzyClient/client?name="+name,String.class);
    }

    public  String zzyError(String name){
        return name+",error!";
    }
}
