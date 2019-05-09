package com.myproject;

import java.util.concurrent.BlockingQueue;

/**
 * @program: myproject
 * @description: 消费者测试类
 * @author: zf
 * @create: 2019-05-09 10:50
 **/
public class CustomerTest implements Runnable {
    private BlockingQueue queue;
    private boolean isRunning = true;

    public CustomerTest(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (isRunning){
                Object data = queue.poll();
                if(null != data){
                    System.out.println("成功消费");
                    System.out.println(String.valueOf(data));
                }else{
                    isRunning = false;
                }
            }
        } catch (Exception e) {

        }
    }
}
