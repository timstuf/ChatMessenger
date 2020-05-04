package com.nure.server;

import com.nure.domain.Message;
import com.nure.parsers.MessageBuilder;
import com.nure.parsers.MessageParser;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ServerThread extends Thread {
    private final Socket socket;
    private final AtomicInteger messageId;
    private final Map<Long, Message> messageList;
    private final BufferedReader in;
    private final PrintWriter out;

    public ServerThread(Socket socket, AtomicInteger messageId, Map<Long, Message> messageList) throws IOException {
        this.socket = socket;
        this.messageId = messageId;
        this.messageList = messageList;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        start();
    }

    @Override
    public void run() {
        try {
            log.debug("New socked thread is starting");
            String requestLine = in.readLine();
            log.debug("Request line : {}", requestLine);
            switch (requestLine) {
                case "GET":
                    log.debug("get");
                    Long lastId = Long.valueOf(in.readLine());
                    log.debug("last message id : {}", lastId);
                    List<Message> messages = messageList.entrySet().stream()
                            .filter(mes -> mes.getKey().compareTo(lastId)>0)
                            .map(Map.Entry::getValue).collect(Collectors.toList());

                    String content = MessageBuilder.createDocument(messages);
                    out.println(content);
                    out.println("END");
                    out.flush();
                    break;
                case "PUT":
                    log.debug("put");
                    requestLine = in.readLine();
                    StringBuilder mesStr = new StringBuilder();
                    while(! "END".equals(requestLine)){
                        mesStr.append(requestLine);
                        requestLine = in.readLine();
                    }
                    log.debug("Message string : {}", mesStr);
                    List<Message> newMessages =MessageParser.getMessageList(mesStr.toString());
                    for (Message message: newMessages) {
                        messageList.put((long) message.getId(), message);
                    }
                    out.println("OK");
                    out.flush();
                    break;
                default:
                    log.info("Unknown request : {}", requestLine);
                    out.println("Unknow request");
                    out.flush();
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            out.println("ERROR HAS OCCURED");
            out.flush();
        }
        finally {
            try {
                socket.close();
                log.debug("Socket closed");
                in.close();
                out.close();
            } catch (IOException e) {
                log.error("Cannot close socket");
                log.error(e.getMessage());
            }
        }
    }
}
