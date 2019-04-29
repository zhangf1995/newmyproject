package com.myproject;

import com.myproject.domain.SysUser;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @program: myproject
 * @description: 测试
 * @author: zf
 * @create: 2019-04-27 10:27
 **/
public class myTest {
    @Test
    public static void main(String[] args) {
        List<SysUser> list = new ArrayList<>();
        for (int a = 0; a < 5; a++) {
            SysUser sysUser = new SysUser();
            sysUser.setId(String.valueOf(a));
            sysUser.setUsername(String.valueOf(a) + "张三");
            sysUser.setPassword(String.valueOf(a) + "张三");
            list.add(sysUser);
        }
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(0));
        SysUser user = new SysUser();
        user.setId("-1");
        user.setUsername("-1张三");
        user.setPassword("-1张三");
        list.add(user);
        System.out.println(list);
        List<SysUser> list1 = list.stream().sorted(Comparator.comparing(SysUser::getId).reversed()).collect(Collectors.toList());
        System.out.println(list1.toString());
    }
}
