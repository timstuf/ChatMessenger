package com.nure.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nure.domain.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBuilderTest {
    @Test
    public void testSerualize() throws JsonProcessingException {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(null, null, "text", LocalDateTime.now()));
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages.get(0)));
        MessageBuilder.createDocument(messages);
    }

    @Test
    public void tryJackson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

    }

}