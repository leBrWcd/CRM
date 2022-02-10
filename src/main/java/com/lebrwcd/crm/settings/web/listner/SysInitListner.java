package com.lebrwcd.crm.settings.web.listner;/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */

import com.lebrwcd.crm.settings.domain.DicValue;
import com.lebrwcd.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * ClassName SysInitListner
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/26
 */

public class SysInitListner implements ServletContextListener {

    private DicService dicService = null;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        //系统初始化
        System.out.println("上下文对象创建start...");
        //上下文域对象
        ServletContext application = event.getServletContext();

        //全局作用域的监听器无法使用自动注入的方式为bean赋值，要想在监听器中能够得到bean只能手动得到spring容器
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
        dicService = context.getBean(DicService.class);
        Map<String, List<DicValue>> map = dicService.getAll();

        //拆解map
        Set<String> set = map.keySet();
        for(String key : set){
            //取数据字典
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存数据字典完成");



        //解析stage2possibility属性资源配置文件
        Map<String, String> pMap = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("conf/stage2possibility");
        //获取key
        Enumeration<String> keys = bundle.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();

            //通过key获取value
            String value = bundle.getString(key);

            //保存到map中
            pMap.put(key,value);
        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);


        System.out.println("上下文对象创建end...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
