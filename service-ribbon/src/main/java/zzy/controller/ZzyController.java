package zzy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zzy.service.ZzyService;

/**
 * Created by zhaozhengyang on 2017/9/26.
 */
@RestController
public class ZzyController {
    @Autowired
    ZzyService zzyService;

    @RequestMapping(value = "/link")
    public String linkClient(@RequestParam  String name){
        return zzyService.linkClient(name);
    }
}
