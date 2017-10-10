package spring.boot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by zhaozhengyang on 2017/9/22.
 */
@SpringBootApplication
public class Application implements EmbeddedServletContainerCustomizer{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8088);
//        container.setContextPath("/test");//修改访问路径，和配置文件修改是类似的。
    }
}
