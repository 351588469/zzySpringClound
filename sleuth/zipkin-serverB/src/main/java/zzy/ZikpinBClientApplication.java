
package zzy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class ZikpinBClientApplication{
    

    public static void main(String[] args){
        SpringApplication.run(ZikpinBClientApplication.class, args);
        
    }
    
    
    /**
     * @Title: home 
     * @Description:zipkin-serverA` 模拟接口
     * @return
     */
    @RequestMapping("/self")
    public String home(){
        return "this is zipkin-serverB";
    }

    /**
     * @Title: info 
     * @Description:调用zipkin-serverB 模拟接口 服务互调(检测链路是否被监控)
     * 				不接入注册中心,直接采取IP+端口+服务名调用
     */
    @RequestMapping("/other")
    public String zipkin(){
        return restTemplate.getForObject("http://localhost:8771/self",String.class);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    
}
