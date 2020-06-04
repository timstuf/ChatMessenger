package com.nure.database.repositories.impl;

import com.nure.database.ConnectionFactory;
import com.nure.database.repositories.Repository;
import com.nure.domain.Chat;
import com.nure.domain.Message;
import com.nure.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ChatRepository implements Repository {
    private static volatile ChatRepository chatRepository;
    private MessageRepository messageRepository = MessageRepository.getInstance();

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        ChatRepository local = chatRepository;
        if (local == null)
            synchronized (ChatRepository.class) {
                local = chatRepository;
                if (local == null) {
                    chatRepository = local = new ChatRepository();
                }
            }
        return local;
    }

    public Map<Chat, List<Message>> getAllMessagesInAllChats() {
        List<Chat> chats = getAllChats();
        Map<Chat, List<Message>> map = new ConcurrentHashMap<>();
        List<Message> messages;
        for (Chat chat : chats) {
            messages = messageRepository.getAllMessagesInChat(chat);
            map.put(chat, messages);
        }
        return map;
    }

    public void saveAllNewMessages(Map<Chat, List<Message>> chatMessagesList) {
        for (Map.Entry<Chat, List<Message>> entry : chatMessagesList.entrySet()) {
            messageRepository.saveAllNewMessages(entry.getValue());
        }
    }

    public void saveIfNotPresent(Chat chat, Map<Chat, List<Message>> map) {
        if (!map.containsKey(chat)) {
            map.put(chat, new ArrayList<>());
        }
    }

    public List<Chat> getAllChats() {
        List<Chat> chats = new ArrayList<>();
        List<User> userList;
        try (Session session = ConnectionFactory.sessionFactory.openSession()) {
            session.beginTransaction();
            userList = session.createQuery("from User").list();
            for (User user : userList) {
                List<User> usersTo = user.getSentMessages().stream().map(Message::getUserTo).collect(toList());
                for (User userTo : usersTo) {
                    Chat chat = new Chat(user, userTo);
                    if (chats.stream().noneMatch(x -> x.equals(chat)))
                        chats.add(chat);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return chats;
    }
}
