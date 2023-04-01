package com.geoffrey.laoye.filter;

import com.alibaba.fastjson.JSON;
import com.geoffrey.laoye.common.BaseContext;
import com.geoffrey.laoye.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@Component
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();

        log.info("拦截到请求：{}",requestURI) ;

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                //放行静态资源，拦截后端的访问路径
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg", //移动端发送短信
                "/user/login" ,//移动端登录
                "/user/logout", //移动端退出登录
                "/user/register" //移动端注册账号
        };
        //2.判断本次请求是否需要处理
        boolean checkFlag = check(urls, requestURI);
        //3.如果不需要处理，则直接放行
        if (checkFlag) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //4.1 客户端判断登录状态，如果已登录，则直接放行
        Long employee =(Long) httpServletRequest.getSession().getAttribute("employee");
        if (employee!= null) {
            log.info("用户已登录，用户employeeId为：{}",httpServletRequest.getSession().getAttribute("employee"));
            BaseContext.setCurrentId(employee);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //4.2 移动端判断登录状态，如果已登录，则直接放行
        Long user =(Long) httpServletRequest.getSession().getAttribute("user");
        if (user!= null) {
            log.info("用户已登录，用户userId为：{}",httpServletRequest.getSession().getAttribute("user"));
            BaseContext.setCurrentId(user);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        log.info("用户未登录");
        //5.如果未登录则返回未登录结果,通过输出流的方式向客户端页面响应数据
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        //JSON.parseObject 是将Json字符串转化为相应的对象
        //JSON.toJSONString 是将对象转化为Json字符串
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
