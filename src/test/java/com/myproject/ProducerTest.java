package com.myproject;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: myproject
 * @description: 生产者测试类
 * @author: zf
 * @create: 2019-05-09 10:49
 **/
public class ProducerTest implements Runnable {

    private volatile boolean isRunning = true;
    private BlockingQueue queue;
    private AtomicInteger count;
    private Random random;
    private static final int TIME_OUT = 1000;

    public ProducerTest(BlockingQueue queue,AtomicInteger count) {
        this.queue = queue;
        random = new Random();
        this.count = count;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Thread.sleep(random.nextInt(TIME_OUT));
                int num = count.incrementAndGet();
                if (!(queue.add(num))) {
                    System.out.println("入队失败");
                } else {
                    System.out.println(Thread.currentThread().getName() + "入队成功" + num);
                }
            }
        } catch (Exception e) {

        }
    }

    public void stop() {
        isRunning = false;
    }
}
