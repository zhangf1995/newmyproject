package com.myproject;

import com.myproject.domain.SysUser;
import org.junit.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @program: myproject
 * @description: 测试
 * @author: zf
 * @create: 2019-04-27 10:27
 **/
public class myTest {
/*    @Test
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
    }*/

    @Test
    public void test1() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.getAndIncrement();
        try {
            System.out.println("开始");
            System.exit(0);
        } catch (Exception e) {

        } finally {
            System.out.println("111");
        }
    }

/*    @Test
    public void test2() {
        int[][] arr = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("====================================================");
        test3(arr, 0, arr.length - 1);
    }*/

    public static void main(String[] args) {
        int[][] arr = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        test3(arr, 0, arr.length - 1);
    }

    @Test
    public static void test3(int[][] arr, int start, int end) {
        if (start > end || end <= 0) return;
        for (int i = start; i <= end; i++) {
            System.out.println(arr[start][i]);
        }
        for (int i = start + 1; i <= end; i++) {
            System.out.println(arr[i][end]);
        }
        for (int i = end - 1; i >= start; i--) {
            System.out.println(arr[end][i]);
        }
        for (int i = end - 1; i > start; i--) {
            System.out.println(arr[i][start]);
        }
        test3(arr, start + 1, end - 1);
    }
}
