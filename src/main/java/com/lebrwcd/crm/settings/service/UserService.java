package com.lebrwcd.crm.settings.service;

import com.lebrwcd.crm.execptions.LoginException;
import com.lebrwcd.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUser();
}
