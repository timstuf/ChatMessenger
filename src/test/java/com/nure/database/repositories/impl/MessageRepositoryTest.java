package com.nure.database.repositories.impl;

import com.nure.domain.Chat;
import com.nure.domain.User;
import com.nure.parsers.MessageBuilder;
import com.nure.parsers.MessageParser;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageRepositoryTest {

    @Test
    void getAllMessagesInChat() {
        MessageRepository messageRepository = MessageRepository.getInstance();
        UserRepository userRepository = UserRepository.getInstance();
        User user1 = userRepository.getUserById(1L).get();
        User user2 = userRepository.getUserById(2L).get();
        MessageBuilder.createDocument(messageRepository.getAllMessagesInChat(new Chat(user1, user2)));
    }
}