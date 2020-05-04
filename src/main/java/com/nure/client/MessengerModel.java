package com.nure.client;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.parsers.MessageParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class MessengerModel {
    private BufferedWriter out;
    private BufferedReader in;
    private Socket socket;
    private String login;
    private UserRepository userRepository = UserRepository.getInstance();

    private ObservableList<String> observableList = FXCollections.observableArrayList();

    public MessengerModel(String login, Socket socket) {
        this.socket = socket;
        this.login = login;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //observableList.addAll(userRepository.findActiveUsersExceptOne(user).stream().map(User::getLogin).collect(Collectors.toList()));
    }
    public String getUserLogin(){
        return login;
    }

    //TODO: add timer to refresh the list every n seconds
    public ObservableList<String> showOnline() {
        try {
            out.write("ONLINE" + "\n");
            out.write(login + "\n");
            out.flush();
            StringBuilder answer = new StringBuilder();
            String line = in.readLine();
            while (!line.equals("END")) {
                answer.append(line);
                line = in.readLine();
            }

            log.debug("answer: {}", answer.toString());
            observableList.addAll(MessageParser.getUsers(answer.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return observableList;

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
