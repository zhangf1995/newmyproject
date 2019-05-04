package com.myproject.controller;

import com.myproject.core.consts.ICodes;
import com.myproject.query.Ret;
import com.myproject.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: myproject
 * @description: 测试类
 * @author: zf
 * @create: 2019-05-04 13:58
 **/
@RestController
@RequestMapping("/redis")
public class TestOneController {

    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/testTwo")
    public Ret testRedis(){
        boolean flag = redisConfig.set("testOne", "testOne");
        if(!flag){
            return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
        }
        return Ret.me();
    }

    @RequestMapping("/testOne")
    public Ret testOne(){
        redisTemplate.opsForValue().set("1111","1111");
        return Ret.me();
    }

    @RequestMapping("/getTestTwo")
    public Ret getTestTwo(){
        String testOne = redisConfig.get("testOne", String.class);
        return Ret.me().setData(testOne);
    }

    @RequestMapping("/getTestOne")
    public Ret getTestOne(){
        Integer val = redisConfig.get("1111", Integer.class);
        return Ret.me().setData(val);
    }
}
