package zzy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
@RefreshScope//重新初始化并载入新的配置内容
public class ConfigClient2Application {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClient2Application.class, args);
	}

	@Value("${foo}")
	String foo;

	@RequestMapping(value = "/config")
	public String hi(){
		return foo;
	}
}
