package  zzy;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ZzyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(ZzyFilter.class);
    @Override//返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型
    public String filterType() {
        //pre:路由之前;round:路由之时;post:路由之后;error:发生错误调用
        return "pre";
    }

    @Override//过滤顺序
    public int filterOrder() {
        return 0;
    }

    @Override//这里可以写逻辑判断,是否要过滤,此时true,永远过滤。
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {//过滤器的具体逻辑
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("ok");
        return null;
    }
}