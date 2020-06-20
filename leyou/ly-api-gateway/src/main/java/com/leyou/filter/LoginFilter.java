package com.leyou.filter;

import com.leyou.common.JwtUtils;
import com.leyou.common.UserInfo;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.leyou.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class LoginFilter extends ZuulFilter {

    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    /**
     * 设置白名单,表示改路径是否走这个过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        // 判断白名单
        // 遍历允许访问的路径
        for (String path : this.filterProperties.getAllowPaths()) {
            // 然后判断是否是符合
            if(requestURI.startsWith(path)){
                System.out.println(path+"====未拦截");
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("zuul=====过滤器");

        // 1 获取token
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();

        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        //2: 校验token
        try {
            //校验通过
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        }catch (Exception e){
            e.printStackTrace();

            // 校验出现异常  返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
        }


        return null;
    }
}
