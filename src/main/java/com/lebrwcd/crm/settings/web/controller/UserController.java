package com.lebrwcd.crm.settings.web.controller;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.settings.service.impl.UserServiceImpl;
import com.lebrwcd.crm.vo.CommonVo;
import com.lebrwcd.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName UserController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
@Controller
@RequestMapping("/user")
public class UserController {

    //创建Service对象
    @Autowired
    private UserServiceImpl userService;
    private final CommonVo commonVo = new CommonVo();

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public Object doLogin(String loginAct, String loginPwd, HttpServletRequest request, HttpServletResponse response) {
        //接收loginAct,loginPwd,ip,前面两个在方法参数列表已经获取
        String ip = request.getRemoteAddr();
        String AutoLoginStatus = request.getParameter("AutoLogin");
        //将密码进行MD5加密
        String loginpwd = MD5Util.getMD5(loginPwd);
        try{
            User user = userService.login(loginAct,loginpwd,ip);
            //如果登录成功，将当前用户存入session域
            request.getSession().setAttribute("user",user);
            //程序执行到这里表示没有异常也就是所有验证都通过
            //登录成功，判断用户是否选择了十天内免登录
            if("ok".equals(AutoLoginStatus)){
                //创建Cookie对象
                Cookie cookie1 = new Cookie("loginAct",loginAct);
                Cookie cookie2 = new Cookie("loginPwd",loginpwd);
                //设置有效时长为10天
                cookie1.setMaxAge(10*24*60*60);
                cookie2.setMaxAge(10*24*60*60);
                //设置Cookie关联路径
                cookie1.setPath(request.getContextPath());
                cookie2.setPath(request.getContextPath());
                //发送Cookie给浏览器
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
            commonVo.setSuccess(true);
            commonVo.setMsg("登录成功");
        }catch (Exception e){
            String msg = e.getMessage();
            System.out.println(msg);
            commonVo.setSuccess(false);
            commonVo.setMsg(msg);
        }
        return commonVo;
    }
}
