package com.nure.database.repositories.impl;

import com.nure.database.ConnectionFactory;
import com.nure.database.repositories.Repository;
import com.nure.domain.Chat;
import com.nure.domain.Message;
import com.nure.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class MessageRepository implements Repository {
    private static volatile MessageRepository messageRepository;

    private MessageRepository() {
    }

    public static MessageRepository getInstance() {
        MessageRepository local = messageRepository;
        if (local == null)
            synchronized (MessageRepository.class) {
                local = messageRepository;
                if (local == null) {
                    messageRepository = local = new MessageRepository();
                }
            }
        return local;
    }

    public void saveMessage(Message message) {
        save(message);
    }

    //get all messages in chat
    public List<Message> getAllMessagesInChat(Chat chat) {
        List<Message> messages = new ArrayList<>();
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            User user1 = session.get(User.class, chat.getUser1().getId());
            User user2 = session.get(User.class, chat.getUser2().getId());
            List<Message> sentBy1 = user1.getSentMessages();
            List<Message> sentBy2 = user2.getSentMessages();
            for (int i = 0; i < sentBy1.size(); i++) {
                Message message = sentBy1.get(i);
                if (message.getUserTo().equals(user2)) messages.add(message);
            }
            for (int i = 0; i < sentBy2.size(); i++) {
                Message message = sentBy2.get(i);
                if (message.getUserTo().equals(user1)) messages.add(message);
            }
            Collections.sort(messages);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return messages;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            messages = session.createQuery("from Message").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return messages;
    }
}
