package com.nure.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Message;
import com.nure.domain.User;
import com.nure.exceptions.NoUserException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MessageParser {
    private static UserRepository userRepository = UserRepository.getInstance();
    public static List<Message> getMessageList(String list){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(list, Message[].class));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public static Message parseMessage(String from, String to, String message) {
        User userFrom = userRepository.getUserByLogin(from).orElseThrow(() -> new NoUserException(from));
        User userTo = userRepository.getUserByLogin(to).orElseThrow(() -> new NoUserException(to));
        return new Message(userFrom, userTo, message, LocalDateTime.now());
    }

    public static List<String> getUsers(String users) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.stream(mapper.readValue(users, User[].class)).map(User::getLogin).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
