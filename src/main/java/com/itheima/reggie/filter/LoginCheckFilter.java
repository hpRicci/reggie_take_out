package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.controller.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 陈万三
 * @create 2023-03-07 21:24
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器, 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求url
        String requestURI = request.getRequestURI();
//        log.info("拦截到请求{}", requestURI);
        //定义需要放行的uri
        String[] uris = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**"
        };

        //2.判断本次请求是否可以放行
        boolean check = check(uris, requestURI); //匹配到返回true
        if (check){
//            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //3.判断登录状态, 如果已登录, 则放行
        if (request.getSession().getAttribute("employee") != null){
//            log.info("用户已登录,id为{}", request.getSession().getAttribute("employee"));

            //ThreadLocal设置当前用户id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //4.如果未登录则返回未登录结果
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    /**
     * 路径匹配, 检查本次请求是否需要放行
     * @param uris
     * @param requestUri
     * @return
     */
    public boolean check(String[] uris, String requestUri){
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri, requestUri);
            if(match){
                return true;
            }
        }
        return false;
    }

}
