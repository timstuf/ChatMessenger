package com.nure.parsers;

import com.nure.domain.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class MessageParserTest {
    @Test
    void test() {
        Message message = new Message(null, null, "haha", LocalDateTime.now());
        List<Message> list = new ArrayList<>();
        list.add(message);
//        String list = " {  \"id\" : 9,  \"userFrom\" : {    \"id\" : 1,    \"login\" : \"adm\"  },  \"userTo\" : {    \"id\" : 2,    \"login\" : \"adm2\"  }, " +
//                " \"text\" : \"from 1 to 2\",  \"sentTime\" : {    \"month\" : \"APRIL\",    \"year\" : 2020,    \"dayOfMonth\" : 20,    \"hour\" : 12,    \"minute\" : 23,    \"monthValue\" : 4," +
//                "    \"nano\" : 687000000,    \"second\" : 46,    \"dayOfWeek\" : \"MONDAY\",    \"dayOfYear\" : 111,    \"chronology\" : {      \"id\" : \"ISO\",      \"calendarType\" : \"iso8601\"    }  }}";
        String lists = MessageBuilder.convertToJson(list);
        System.out.println(lists);
        List<Message> res = MessageParser.getMessageList(lists);
        System.out.println(res.get(0));
        //System.out.println(Arrays.toString(message));

    }

}