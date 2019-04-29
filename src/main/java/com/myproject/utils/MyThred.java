package com.myproject.utils;

import com.myproject.domain.SysUser;
import com.myproject.service.ISysUserService;

import java.util.List;

/**
 * @program: myproject
 * @description: 自定义线程
 * @author: zf
 * @create: 2019-04-29 10:48
 **/
public class MyThred extends Thread{

    private ISysUserService sysUserService;
    private List<SysUser> users;

    public MyThred(ISysUserService sysUserService,List<SysUser> users){
        this.sysUserService = sysUserService;
        this.users = users;
    }

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        users.forEach(sysUser -> sysUserService.save(sysUser));
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName()+"花费:"+(endTime-beginTime)/1000+"秒");
    }
}
