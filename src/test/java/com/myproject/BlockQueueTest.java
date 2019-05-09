package com.myproject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: myproject
 * @description: 阻塞队列测试
 * @author: zf
 * @create: 2019-05-09 14:37
 **/
public class BlockQueueTest {

    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new LinkedBlockingDeque(10);
        ProducerTest p1 = new ProducerTest(queue,count);
        ProducerTest p2 = new ProducerTest(queue,count);
        ProducerTest p3 = new ProducerTest(queue,count);
        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(p3);
        CustomerTest customerTest = new CustomerTest(queue);
        Thread thread = new Thread(customerTest);
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(2000);
        p1.stop();
        p2.stop();
        p3.stop();
        Thread.sleep(1000);
        thread.start();
    }
}
