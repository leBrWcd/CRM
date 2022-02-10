package com.lebrwcd.crm.settings.dao;

import com.lebrwcd.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */
public interface UserDao {
    User login(Map<String, String> map);

    List<User> getUser();
}
