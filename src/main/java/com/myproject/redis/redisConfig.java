package com.myproject.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @program: myproject
 * @description: redis配置类
 * @author: zf
 * @create: 2019-05-04 11:41
 **/
@Configuration
public class redisConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 往redis中set
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T value) {
        try {
            //任意类型转换成String
            String val = beanToString(value);

            if (val == null || val.length() <= 0) {
                return false;
            }

            redisTemplate.opsForValue().set(key, val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从redis中get
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (null == o) {
                return null;
            } else {
                return stringToBean(String.valueOf(o), clazz);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * string转bean
     *
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }

    /**
     * @param Every T values
     * @param T     任意类型
     * @return String
     */
    private <T> String beanToString(T value) {

        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

}
