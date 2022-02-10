package com.lebrwcd.crm.settings.service.impl;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import com.lebrwcd.crm.execptions.LoginException;
import com.lebrwcd.crm.settings.dao.UserDao;
import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.settings.service.UserService;
import com.lebrwcd.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName UserServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    //业务代码
    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException{
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("账号密码错误,请重新输入");
        }
        //程序运行到这里表示账号密码正确，继续向下验证
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号时间失效");
        }
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("该IP不可访问");
        }

        return user;
    }

    @Override
    public List<User> getUser() {
        return userDao.getUser();
    }
}
