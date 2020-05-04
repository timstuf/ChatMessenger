package com.nure.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nure.domain.Message;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MessageParser {

    public static List<Message> getMessageList(String list){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(list, Message[].class));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
