package com.myproject;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: myproject
 * @description: 排序测试
 * @author: zf
 * @create: 2019-05-08 09:33
 **/
public class paixuTest {
    //冒泡排序
    @Test
    public void maopaoTest() {
        int[] arr = {5, 2, 6, 1, 7, 4, 8, 3, 9, 0};
        for (int i = 1; i < arr.length; i++) {
            boolean flag = true;
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }

    //快排
    public static void main(String[] args) {
   /*     int[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};
        kuaiPaiTest(0, arr.length - 1, arr);
        for (int i : arr) {
            System.out.println(i);
        }*/
        deadSyn();
    }

    @Test
    public static void kuaiPaiTest(int begin, int end, int[] arr) {
        if (begin > end) {
            return;
        }
        int i = begin;
        int j = end;
        int temp = arr[begin];
        while (i != j) {
            while (arr[j] >= temp && i < j) {
                j--;
            }
            while (arr[i] <= temp && i < j) {
                i++;
            }
            if (i < j) {
                int sign = arr[j];
                arr[j] = arr[i];
                arr[i] = sign;
            }
        }
        arr[begin] = arr[i];
        arr[i] = temp;
        kuaiPaiTest(begin, i - 1, arr);
        kuaiPaiTest(i + 1, end, arr);
    }

    @Test
    public static void deadSyn() {
        Integer a = 1;
        Integer b = 2;
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            synchronized (a) {
                try {
                    Thread.sleep(1000);
                    System.out.println("thread1拿不到b");
                } catch (InterruptedException e) {
                }
                synchronized (b) {
                    System.out.println("thread1");
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (b) {
                try {
                    Thread.sleep(1000);
                    System.out.println("thread2拿不到a");
                } catch (InterruptedException e) {
                }
                synchronized (a) {
                    System.out.println("thread2");
                }
            }
        }).start();
    }
}
