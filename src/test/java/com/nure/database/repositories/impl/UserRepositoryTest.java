package com.nure.database.repositories.impl;

import com.nure.domain.Chat;
import com.nure.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void getAllChats() {
        UserRepository userRepository = UserRepository.getInstance();
        List<Chat> chats =
                userRepository.getAllChats();
        chats.size();
    }
    @Test
    void checkForEquality(){
        UserRepository userRepository = UserRepository.getInstance();
        User user1 = userRepository.getUserById(1L).get();
        User user2 = userRepository.getUserById(2L).get();
        Chat chat1 = new Chat(user1, user2);
        Chat chat2 = new Chat(user2, user1);
        assertTrue(chat1.equals(chat2));
    }
}