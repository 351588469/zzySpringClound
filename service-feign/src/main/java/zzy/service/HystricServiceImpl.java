package zzy.service;

import org.springframework.stereotype.Component;

/**
 * Created by zhaozhengyang on 2017/10/9.
 */
@Component
public class HystricServiceImpl implements FeignService {

    @Override
    public String linkClient(String name) {
        return "sorry "+name;
    }
}
