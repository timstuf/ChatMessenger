package com.nure.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nure.domain.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class MessageBuilderTest {
    @Test
    public void testSerualize() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(null, null, "text", LocalDateTime.now()));
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages.get(0)));
        MessageBuilder.convertToJson(messages);
    }

    @Test
    public void tryJackson() {
        ObjectMapper mapper = new ObjectMapper();

    }

}