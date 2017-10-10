package zzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 服务与服务的通讯是基于http restful的。Spring cloud有两种服务调用方式，一种是ribbon+restTemplate，另一种是feign.
 * ribbon是一个负载均衡客户端，可以很好的控制htt和tcp的一些行为。Feign默认集成了ribbon。
 *
 * mod on 2017.10.9 在ribbon中添加断路器Hystrix
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix//开启Hystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
public class ServiceRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRibbonApplication.class, args);
	}

	/**
	 *RestTemplate有点类似于一个WebService客户端请求的模版，可以调用http请求的WebService，并将结果转换成相应的对象类型。
	 */
	@Bean//创建RestTemplate实例
	@LoadBalanced//开启负载均衡能力

	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
