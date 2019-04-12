package com.myproject.controller;

import com.myproject.consts.WebConsts;
import com.myproject.es.esDomain.EsTest;
import com.myproject.es.esQueryUtils.EsTestQuery;
import com.myproject.es.esService.EsTestService;
import com.myproject.query.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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

    @RequestMapping("/test1")
    public Integer test1(){
        int a = 1/0;
        return a;
    }

    @RequestMapping("/esSave")
    public void save(){
        EsTest esTest = new EsTest();
        esTest.setId(new Date().getTime());
        esTest.setTitle("李四中");
        esTest.setBrand("李四皮革");
        esTest.setCategory("张张嘴");
        esTest.setPrice(3.01);
        esTest.setImages("1e1ee1e");
        try{
            esTestService.save(esTest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //简单查询
    @RequestMapping("/esSearch/{id}")
    public Ret esSearch(@PathVariable Long id){
        EsTest esTest = esTestService.findById(id).get();
        return Ret.me().setData(esTest);
    }

    //高级查询一
    @RequestMapping("/esSearchOne")
    public Ret esSearchOne(){
        List<EsTest> list = esTestQuery.elasticSerchTest();
        return Ret.me().setData(list);
    }
}
