package com.myproject.controller;

import com.myproject.consts.WebConsts;
import com.myproject.core.consts.ICodes;
import com.myproject.domain.Order;
import com.myproject.domain.Stock;
import com.myproject.es.esDomain.EsTest;
import com.myproject.es.esQueryUtils.EsTestQuery;
import com.myproject.es.esService.EsTestService;
import com.myproject.query.Ret;
import com.myproject.rabbitmq.provider.FirstSender;
import com.myproject.service.IOrderService;
import com.myproject.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @program: myproject
 * @description: 测试接口
 * @author: zf
 * @create: 2019-04-07 10:36
 **/
@RestController
@RequestMapping(WebConsts.URL_TEST)
public class TestController {

    @Autowired
    private EsTestService esTestService;
    @Autowired
    private EsTestQuery esTestQuery;
    @Autowired
    private FirstSender firstSender;
    @Autowired
    private IStockService stockService;
    @Autowired
    private IOrderService orderService;

/*
    public static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

    static {
        map.put(1, 10);
    }
*/
    @RequestMapping("/test1")
    public Integer test1() {
        int a = 1 / 0;
        return a;
    }

    @RequestMapping("/esSave")
    public void save() {
        EsTest esTest = new EsTest();
        esTest.setId(System.currentTimeMillis());
        esTest.setTitle("李四中");
        esTest.setBrand("李四皮革");
        esTest.setCategory("张张嘴");
        esTest.setPrice(3.01);
        esTest.setImages("1e1ee1e");
        try {
            esTestService.save(esTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //简单查询
    @RequestMapping("/esSearch/{id}")
    public Ret esSearch(@PathVariable Long id) {
        EsTest esTest = esTestService.findById(id).get();
        return Ret.me().setData(esTest);
    }

    //高级查询一
    @RequestMapping("/esSearchOne")
    public Ret esSearchOne() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        List<EsTest> list = esTestQuery.elasticSerchTest();
        return Ret.me().setData(list);
    }

    //rabbitmq测试
    @RequestMapping("/rqTest")
    public void rqTest() {
        firstSender.send("one测试");
        firstSender.sendTwo("two测试");
    }

    //延迟测试
    @RequestMapping("/delayTest")
    public void delayTest() {
        firstSender.sendDelay("延迟测试");
    }

    //普通测试(秒杀)
    @RequestMapping("/simpleMiaoShaTest")
    public Ret simpleTest(int id) {
        Stock stock = new Stock();
        //库存为0返回库存不足
        synchronized (Object.class) {
            stock = stockService.get(id);
            if (0 == stock.getCount()) {
                return Ret.me().setSuccess(false).setData(ICodes.FAILED);
            }
            //减库存
            stock.setCount(stock.getCount() - 1);
            stockService.update(stock);
        }
        //创建订单
        Order order = new Order();
        int oid = new Random().nextInt(1000000);
        order.setId(oid);
        order.setGoodId(id);
        order.setCount(1);
        orderService.save(order);

        //暂时不做后续操作
        return Ret.me();
    }

/*    //优化秒杀(只是测试而已)
    @Transactional
    @RequestMapping("/newMiaoShaTest")
    public Ret newMiaoShaTest(int id) {
        Integer num = map.get(id);
        if (num <= 0) {
            return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
        }
        map.put(id, num - 1);
        Stock stock = stockService.get(id);
        stock.setCount(stock.getCount() - 1);
        //创建订单
        Order order = new Order();
        order.setId(1);
        order.setGoodId(id);
        order.setCount(1);
        orderService.save(order);
        stockService.update(stock);
        return Ret.me();
    }*/
}
