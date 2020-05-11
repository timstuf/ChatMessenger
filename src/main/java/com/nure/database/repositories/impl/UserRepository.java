package com.nure.database.repositories.impl;

import com.nure.database.ConnectionFactory;
import com.nure.database.repositories.Repository;
import com.nure.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
public class UserRepository implements Repository {
    private static volatile UserRepository userRepository;

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository local = userRepository;
        if (local == null)
            synchronized (UserRepository.class) {
                local = userRepository;
                if (local == null) {
                    userRepository = local = new UserRepository();
                }
            }
        return local;
    }

    public String tryLogin(String name, String password) {
        Optional<User> user = getUserByLogin(name);
        if (!user.isPresent()) return "There is no user with specified login...";
        if (!user.get().getPassword().equals(password)) return "Password is wrong";
        return "";
    }
    private boolean isLoginValid(String login){
       return !getUserByLogin(login).isPresent();
    }
    public void saveUser(User user) {
        save(user);
    }

    public String registerUser(String login, String password) {
        User user = new User(login, password);
        if(!isLoginValid(user.getLogin())) return "User with specified login already exists";
        try(Session session = ConnectionFactory.sessionFactory.openSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }catch(Exception e){
            log.error(e.getMessage());
            return "Error: "+e.getMessage();
        }
        return "";
    }
    public void logEveryoneOut(){
        List<User> users;
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            users = session.createQuery("from User").list();
            for (User user:users) {
                user.setStatus(User.Status.LOGGED_OUT);
                session.save(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<User> findActiveUsersExceptOne(User author){
        List<User> users = new ArrayList<>();
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, author.getId());
            users = session.createQuery("from User where status='ACTIVE'").list();
            users = users.stream().filter(x->!x.equals(user)).collect(toList());
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return users;
    }
    public Optional<User> getUserById(Long id) {
        User user = null;
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            return Optional.of(user);
        } else return Optional.empty();
    }

    public Optional<User> getUserByLogin(String login) {
        User user = null;
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            user = (User) session.createQuery("from User where login = '" + login + "'").list().get(0);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            return Optional.of(user);
        } else return Optional.empty();
    }
}
