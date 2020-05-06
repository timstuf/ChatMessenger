package com.nure.client;

import com.nure.client.timers.UpdateList;
import com.nure.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.*;

@Getter
@Slf4j
public class MessengerModel {
    private BufferedWriter out;
    private BufferedReader in;
    private Socket socket;
    private String login;
    @Setter
    private Map<String, String> chatMessages = new HashMap<>();
    private MessengerController controller;
    private List<String> onlineUsers = new ArrayList<>();
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    public MessengerModel(String login, Socket socket, MessengerController controller) {
        this.socket = socket;
        this.login = login;
        this.controller = controller;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Timer().scheduleAtFixedRate(new UpdateList(this),
                Constants.DELAY, Constants.PERIOD);
    }

    public void setOnlineUsers(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
        observableList.setAll(onlineUsers);
    }

    public ObservableList<String> showOnline() {
        observableList.setAll(onlineUsers);
        return observableList;
    }

    public String showChatMessages(String anotherUser) {
        return chatMessages.get(anotherUser);
    }

    @SneakyThrows
    public void sendMessage(String text, String from, String to) {
        out.write("PUT" + '\n');
        out.write(from + '\n');
        out.write(to + '\n');
        out.write(text + '\n');
        out.write("END" + '\n');
        out.flush();
    }
}
