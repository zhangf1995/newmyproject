package com.myproject.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    public static void main(String[] args) throws Exception {
        //首先保证数据库中有对应的表，远程的数据库有个测试版t_test
        //配置resources/generator/generatorConfig.xml文件中的<table tableName="t_test" ...>，最主要是根据实际需要修改需要生成代码的表格名，此处目前是t_test,如果需要批量生成，则可以用%来做模糊匹配，比如要生成所有系统模块的代码，可以替换为"t_sys_%"
        //注意：以下代码分成两次来执行
        //第一次执行 ： 生成项目基本结构，包含domain和query对象、 mapper接口和mapper.xml、service接口和实现、
        //controller、前端的index/form/show视图和对应的js ，另外，同时生成业务常量类
        //注意点：该次执行所有代码会执行覆盖生成，如果已经基于生成代码做了逻辑添加，此处慎用。但是，mapper.xml却是增量添加，因此又会出现重复代码，建议可以先删除原来的mapper.xml再执行覆盖生成。
        generateProject();
        //generatedBisConsts();

        //第二次执行，第一次完成后，刷新整个cerp项目，将生成的代码刷新出来，然后再注释第一次调用的代码，放开下面的代码，调用以下代码在控制台输出常量国际化代码，
        //然后，拷贝需要的内容到国际化配置文件messages_zh_CN.properties中的常量部分
        //另外，domain类的字段国际化常量生成在i18n文件夹下的domain-local-temp.txt中，也需要将里面的代码拷贝到messages_zh_CN.properties中的domain部分
        //generateConstLocale();
    }

    private static void generateProject() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(
                CodeGenerator.class.getResourceAsStream("/generator/generatorConfig.xml"));
        ShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

/*    private static void generateConstLocale() {
        System.out.println("#Business Consts");
        Map<Class, List<KV<Integer, ConstMsg>>> bismap = ConstUtils.me().getBISMAP();
        for (Class clazz : bismap.keySet()) {
            System.out.println("#" + clazz.getSimpleName());
            List<KV<Integer, ConstMsg>> list = bismap.get(clazz);
            for (KV<Integer, ConstMsg> kv : list) {
                Integer k = kv.getK();
                ConstMsg v = kv.getV();
                String constLocale = v.getKeyCode() + "=" + v.getName();
                System.out.println(constLocale);
            }
            System.out.println();
        }

        System.out.println("#Message Consts");
        Map<Integer, ConstMsg> codesmap = ConstUtils.me().getCODESMAP();
        for (Integer msgCode : codesmap.keySet()) {
            ConstMsg constMsg = codesmap.get(msgCode);
            String constLocale = constMsg.getKeyCode() + "=" + constMsg.getName();
            System.out.println(constLocale);
        }

    }

    private static void generatedBisConsts() {
        String userDir = System.getProperty("user.dir");
        userDir = userDir.replace("\\", "/");
        FreemarkerUtil instance = FreemarkerUtil.getInstance("2.3.26-incubating", "/generator/templates/");
        Map<String, List<BisConst>> srcConstMap = Ext.bisConstMap;
        for (String constName : srcConstMap.keySet()) {
            List<BisConst> constValueList = srcConstMap.get(constName);
            String targetFilePath = userDir + "/src/main/java/cn/cxby/cerp/core/consts/bis/" + constName + ".java";
            Map<String, Object> bisConstMap = new HashMap<String, Object>();
            bisConstMap.put("constName", constName);
            bisConstMap.put("valueList", constValueList);
            instance.fprint(bisConstMap, "BisConsts.ftl", targetFilePath);
        }

    }*/
}
