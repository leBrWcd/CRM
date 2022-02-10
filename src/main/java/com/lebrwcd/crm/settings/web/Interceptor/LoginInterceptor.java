package com.lebrwcd.crm.settings.web.Interceptor;/**
 * @author lebrwcd
 * @date 2022/1/14
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName LoginInterceptor
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/14
 */

/**
 * 验证请求是否合法
 *  preHandler返回true才可以访问控制器，如果请求不成功，不放行
 */
public class LoginInterceptor implements HandlerInterceptor {

   private boolean flag = false;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println("进入拦截器验证是否已登录过");
        //先获得session域对象的用户对象
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            System.out.println("没有登陆过，跳转到登录页面");
            //重定向
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }else{
           flag = true;
        }
        return flag;
    }
}
