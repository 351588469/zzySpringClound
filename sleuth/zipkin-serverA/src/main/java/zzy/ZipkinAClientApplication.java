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
public class ZipkinAClientApplication{
    

    public static void main(String[] args){
        SpringApplication.run(ZipkinAClientApplication.class, args);
        
    }
    @RequestMapping("/self")
    public String home(){
        return "this is zipkin-serverA";
    }

    /**
     * @Title: info
     * @Description:调用zipkin-serverB 模拟接口 服务互调(检测链路是否被监控)
     * 不接入注册中心,直接采取IP+端口+服务名调用
     * @return
     */
    @RequestMapping("/other")
    public String zipkin(){
        return restTemplate.getForObject("http://localhost:8772/self",String.class);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 作用等同于 spring.sleuth.sampler.percentage=1
     * percentage决定日志记录发送给采集器的概率
     * @return
     */
//    @Bean
//    public AlwaysSampler defaultSampler() {
//        return new AlwaysSampler();
//    }

}
