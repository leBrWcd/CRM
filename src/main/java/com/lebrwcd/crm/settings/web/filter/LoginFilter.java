package com.lebrwcd.crm.settings.web.filter;/**
 * @author lebrwcd
 * @date 2022/1/14
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * ClassName LoginFilter
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/14
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String path = request.getServletPath();
        //登录页和登录方法放行
        if("/login.jsp".equals(path) || "/user/login.do".equals(path)){
            filterChain.doFilter(request, response);
        }else{
            //其他页面
            if(user == null){
                //跳转到登录页面
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}
