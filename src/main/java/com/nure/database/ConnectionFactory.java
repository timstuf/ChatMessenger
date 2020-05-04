package com.nure.database;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;

@Slf4j
public class ConnectionFactory {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/messanger";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "Admin123";

    public final static SessionFactory sessionFactory;
    static{
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
    }
}