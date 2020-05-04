package com.nure.server;

import com.nure.database.repositories.impl.MessageRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Message;
import com.nure.parsers.MessageBuilder;
import com.nure.parsers.MessageParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ServerThread extends Thread {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final MessageRepository messageRepository = MessageRepository.getInstance();
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

    @SneakyThrows
    @Override
    public void run() {
        log.debug("New socked thread is starting");
        try {
            while (true) {
                Thread.sleep(1000);
                String response;
                String login;

                String requestLine = in.readLine();
                log.debug("Request line : {}", requestLine);
                switch (requestLine) {
                    //TODO: check if user is not already online
                    case "CONNECT":
                        log.debug("connect");
                        login = in.readLine();
                        String password = in.readLine();
                        response = userRepository.tryLogin(login, password);
                        if (response.equals("")) response = "OK";
                        out.println(response);
                        out.flush();
                        break;
                    case "ONLINE":
                        log.debug("online");
                        String except = in.readLine();
                        response = MessageBuilder.getOnlineExcept(except);
                        out.println(response);
                        out.println("END");
                        out.flush();
                        break;
                    case "GET":
                        log.debug("get");
                        Long lastId = Long.valueOf(in.readLine());
                        log.debug("last message id : {}", lastId);
                        List<Message> messages = messageList.entrySet().stream()
                                .filter(mes -> mes.getKey().compareTo(lastId) > 0)
                                .map(Map.Entry::getValue).collect(Collectors.toList());

                        String content = MessageBuilder.createDocument(messages);
                        out.println(content);
                        out.println("END");
                        out.flush();
                        break;
                    case "PUT":
                        log.debug("put");
                        String from = in.readLine();
                        String to = in.readLine();
                        requestLine = in.readLine();
                        StringBuilder mesStr = new StringBuilder();
                        while (!"END".equals(requestLine)) {
                            mesStr.append(requestLine);
                            requestLine = in.readLine();
                        }
                        log.debug("Message string : {}", mesStr);
                        Message message = MessageParser.parseMessage(from, to, mesStr.toString());
                        messageRepository.saveMessage(message);
//                        out.println("OK");
//                        out.flush();
                        break;
                    default:
                        log.info("Unknown request : {}", requestLine);
                        out.println("Unknown request");
                        out.flush();
                        break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            out.println("ERROR HAS OCCURRED");
            out.flush();
        } finally {
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

