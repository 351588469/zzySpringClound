package zzy.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhaozhengyang on 2017/9/26.
 */
@FeignClient(value = "zzyClient",fallback = HystricServiceImpl.class)
public interface FeignService {
    @RequestMapping(value = "/client",method = RequestMethod.GET)
    String linkClient(@RequestParam(value = "name") String name);

}
