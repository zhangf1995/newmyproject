package com.myproject.utils;

import com.myproject.domain.SysUser;
import com.myproject.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: myproject
 * @description: 自定义线程1 原生的jdbc
 * @author: zf
 * @create: 2019-04-29 11:50
 **/
public class MyThreadOne extends Thread {

    private ISysUserService sysUserService;
    private List<SysUser> users;

    public MyThreadOne(ISysUserService sysUserService, List<SysUser> users) {
        this.sysUserService = sysUserService;
        this.users = users;
    }

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        String url = "jdbc:mysql://127.0.0.1:3306/myproject";
        String name = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);//获取连接
            conn.setAutoCommit(false);//关闭自动提交，不然conn.commit()运行到这句会报错
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // sql前缀
        String prefix = "INSERT INTO t_sys_user (id,password,username) VALUES ";
        try {
            // 保存sql后缀
            StringBuffer suffix = new StringBuffer();
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            // 比起st，pst会更好些
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement("");//准备执行语句
            // 外层循环，总提交事务次数
            for (int a = 0; a < 10; a++) {
                List<SysUser> users1 = users.subList(a * 10000, (a+1)*10000);
                // 第j次提交步长
                for (SysUser sysUser : users1) {
                    // 构建SQL后缀
                    suffix.append("('" + sysUser.getId() + "','" + sysUser.getPassword() + "','" + sysUser.getUsername() + "'),");
                }
                // 构建完整SQL
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                // 添加执行SQL
                pst.addBatch(sql);
                // 执行操作
                pst.executeBatch();
                // 提交事务
                conn.commit();
                // 清空上一次添加的数据
                suffix = new StringBuffer();
            }
            // 头等连接
            pst.close();
            conn.close();
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "花费:" + (endTime - beginTime) / 1000 + "秒");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
