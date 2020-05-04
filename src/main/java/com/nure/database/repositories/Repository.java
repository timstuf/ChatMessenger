package com.nure.database.repositories;

import com.nure.database.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.sql.Connection;

public interface Repository {
    default Connection getConnection(){
        return ConnectionFactory.getConnection();
    }
    default <T> void save(T t){
        try(Session session = ConnectionFactory.sessionFactory.openSession()){
            session.beginTransaction();
            session.save(t);
            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
