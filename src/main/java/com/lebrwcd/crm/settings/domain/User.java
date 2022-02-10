package com.lebrwcd.crm.settings.domain;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import org.springframework.stereotype.Component;

/**
 * ClassName User
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
@Component
public class User {

    private String id;          //编号 主键
    private String loginAc;     //登录账号
    private String name;        //真实姓名
    private String loginPwd;    //登录密码
    private String email;       //邮箱
    private String expireTime;  //失效时间 19 "yyyy-MM-dd HH:mm:ss"
    private String lockState;   //锁定状态  0：锁定，1：启用
    private String deptno;      //部门编号
    private String allowIps;    //允许IP
    private String createTime;  //创建时间 19
    private String createBy;    //创建者
    private String editTime;    //编辑时间
    private String editBy;      //编辑者

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAc() {
        return loginAc;
    }

    public void setLoginAc(String loginAc) {
        this.loginAc = loginAc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}


