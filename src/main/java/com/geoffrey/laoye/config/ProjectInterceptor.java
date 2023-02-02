//package com.geoffrey.reggie.config;
//
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
////使用过滤器使得前后端耦合度过高，建议使用拦截器在这边做逻辑处理（太菜还没实现成功）
//@Component
//public class ProjectInterceptor implements HandlerInterceptor {
//    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        //1.获取本次请求的URI
//        String requestURI = request.getRequestURI();
//
//        String[] urls = {"/employee/login",
//                "/employee/logout",
//                "/backend/**",
//                "/front/**"};
//
//        if(check(urls,requestURI)){
//            return true;
//        }
//        //4.判断登录状态，如果已登录，则直接放行
//        if (request.getSession().getAttribute("employee") != null) {
//            return true;
//        }
//
//        request.setAttribute("msg","账户还未登录！！！");
//        request.getRequestDispatcher("backend/page/login/login.html").forward(request,response);
//        return false;
//
//    }
//    public boolean check(String[] urls, String requestURI) {
//        for (String url : urls) {
//            boolean match = PATH_MATCHER.match(url, requestURI);
//            if (match) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
