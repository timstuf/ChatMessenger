package com.nure.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nure.database.repositories.impl.MessageRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Message;
import com.nure.domain.User;
import com.nure.exceptions.NoUserException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageBuilder {
    public static String loadAllPreviousMessages(){
        MessageRepository messageRepository = MessageRepository.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String result =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageRepository.getAllMessages());
            log.debug("Serialized messages : {}", result);
            return result;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    public static String getOnlineExcept(String login) {
        UserRepository userRepository = UserRepository.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = userRepository.getUserByLogin(login).orElseThrow(() -> new NoUserException(login));
            String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userRepository.findActiveUsersExceptOne(user));
            log.debug("Serialized online users : {}", result);
            return result;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return "";
    }
    public static List<Message> loadAllPreviousMessagesInList(){
        MessageRepository messageRepository = MessageRepository.getInstance();
        return messageRepository.getAllMessages();
    }

    public static String createDocument(List<Message> messages){
        StringBuilder strMessages = new StringBuilder();
        for (Message message:messages) {
            String mes = message.getSentTime()+ "    "+message.getUserFrom().getLogin()+":    "+message.getText()+'\n';
            log.debug("Message: {}", mes);
            strMessages.append(mes);
        }
        return strMessages.toString();
    }
}
