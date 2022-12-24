package com.example.coursework.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DaoFactory {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/course_work";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private static DaoFactory instance;
    public DaoFactory() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

    public FileDao getFileDao() {
        return new FileDaoImpl();
    }
}
