package com.example.hedadmin;

import com.example.hedadmin.entity.Admin;        // 改成 Admin
import com.example.hedadmin.mapper.AdminMapper;  // 改成 AdminMapper
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;  // 改成 AdminMapper

    @Test
    public void testConnection() throws SQLException {
        System.out.println("========== 测试数据库连接 ==========");
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ 数据库连接成功！");
            System.out.println("数据库: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("URL: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testQueryAdmin() {
        System.out.println("========== 测试查询 admin 表 ==========");
        try {
            List<Admin> list = adminMapper.selectList(null);
            System.out.println("✅ 查询成功！共 " + list.size() + " 条记录");
            list.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("❌ 查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testMyBatisPlus() {
        System.out.println("========== 测试 MyBatis-Plus ==========");
        try {
            Long count = adminMapper.selectCount(null);
            System.out.println("✅ MyBatis-Plus 正常，总记录数: " + count);
        } catch (Exception e) {
            System.err.println("❌ MyBatis-Plus 异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}