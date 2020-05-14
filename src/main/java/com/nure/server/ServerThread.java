package com.nure.server;

import com.nure.database.repositories.impl.ChatRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Chat;
import com.nure.domain.Message;
import com.nure.parsers.MessageBuilder;
import com.nure.parsers.MessageParser;
import javafx.application.Platform;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServerThread extends Thread {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final Socket socket;
    private final Map<Chat, List<Message>> messages;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ClientList clientList;
    private String user;
    private final Controller controller;

    public ServerThread(Socket socket, Map<Chat, List<Message>> messageList, ClientList clientList, Controller controller) throws IOException {
        this.socket = socket;
        this.messages = messageList;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        this.clientList = clientList;
        this.controller = controller;
        start();
    }

    @SneakyThrows
    @Override
    public void run() {
        log.debug("New socked thread is starting");
        try {
            while (true) {
                String login, from, to, response, password;
                Chat chat;
                String requestLine = in.readLine();
                //log.debug("Request line : {}", requestLine);
                switch (requestLine) {
                    //TODO: check if user is not already online
                    case "CONNECT":
                        //log.debug("connect");
                        login = in.readLine();
                        password = in.readLine();
                        response = userRepository.tryLogin(login, password);
                        if (response.equals("")) {
                            response = "OK";
                            clientList.addName(login);
                            user = login;
                        }
                        out.println(response);
                        out.flush();
                        break;
                    case "NEW":
                        //log.debug("new");
                        login = in.readLine();
                        password = in.readLine();
                        response = userRepository.registerUser(login, password);
                        if (response.equals("")) {
                            response = "OK";
                            clientList.addName(login);
                            user = login;
                        }
                        out.println(response);
                        out.flush();
                        break;
                    case "ONLINE":
                        //log.debug("online");
                        String except = in.readLine();
                        response = clientList.getOnlineExcept(user);
                        out.println(response + "\n");
                        out.println("END");
                        out.flush();
                        break;
                    case "GET":
                        //log.debug("get");
                        from = in.readLine();
                        to = in.readLine();
                        chat = Chat.asObject(from + " --- " + to);
                        out.println(MessageBuilder.convertToJson(messages.get(chat)) + "\n");
                        out.println("END");
                        out.flush();
                        break;
                    case "PUT":
                        log.debug("put");
                        from = in.readLine();
                        to = in.readLine();
                        requestLine = in.readLine();
                        StringBuilder mesStr = new StringBuilder();
                        while (!"END".equals(requestLine)) {
                            mesStr.append(requestLine);
                            requestLine = in.readLine();
                        }
                        log.debug("Message string : {}", mesStr);
                        Message message = MessageParser.parseMessage(from, to, mesStr.toString());
                        chat = Chat.asObject(from + " --- " + to);
                        chatRepository.saveIfNotPresent(chat, messages);
                        messages.get(chat).add(message);
                        Platform.runLater(() -> {
                            List<Message> forCont = new ArrayList<>();
                            forCont.add(message);
                            controller.addMessageToChat(chat, MessageBuilder.showPrettyInChat(forCont));
                            controller.showOnline(messages.keySet());
                        });
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
                clientList.removeName(user);
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

