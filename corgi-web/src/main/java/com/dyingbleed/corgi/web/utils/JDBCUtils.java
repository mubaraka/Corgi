package com.dyingbleed.corgi.web.utils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 李震 on 2018/5/14.
 */
public class JDBCUtils {

    /**
     * 测试 MySQL 数据库连接
     *
     * @param url
     * @param username
     * @param password
     *
     * */
    public static void testMySQLConnection(String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, username, password); Statement statement = connection.createStatement()) {
            statement.execute("select 1");
        }
    }

    /**
     * 显示所有数据库
     *
     * @param url
     * @param username
     * @param password
     *
     * @return 数据库列表
     *
     * */
    public static List<String> showDatabases(String url, String username, String password) throws ClassNotFoundException, SQLException {
        List<String> databases = new LinkedList<>();

        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, username, password); Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("show databases");
            while (rs.next()) {
                databases.add(rs.getString(1));
            }
        }

        return databases;
    }

    /**
     * 显示所有表
     *
     * @param url
     * @param username
     * @param password
     * @param database
     *
     * @return 表列表
     *
     * */
    public static List<String> showTables(String url, String username, String password, String database) throws ClassNotFoundException, SQLException {
        List<String> tables = new LinkedList<>();

        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, username, password); Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("show tables in " + database);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }

        return tables;
    }

}
