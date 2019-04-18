package com.myproject;

import com.myproject.domain.SysUser;

/**
 * @program: myproject
 * @description: 本地线程绑定user
 * @author: zf
 * @create: 2019-04-04 14:03
 **/
public class UserContext {
    private static final ThreadLocal<SysUser> local = new ThreadLocal<>();

    public void setSysUser(SysUser user) {
        SysUser sysUser = local.get();
        if (null == sysUser) {
            local.set(user);
        }
    }

    public SysUser getSysUser() {
        return null == local.get() ? new SysUser() : local.get();
    }
}
