package com.myproject.domain;

/**
 * t_sys_user
 */
public class SysUser extends BaseDomain {
    /**
     * 
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 
     * @return id 
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 用户名
     * @return username 
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户名
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 密码
     * @return password 
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}