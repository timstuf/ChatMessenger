package com.nure.client.timers;

import com.nure.client.MessengerModel;
import com.nure.parsers.MessageParser;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;


@Slf4j
public class UpdateList extends TimerTask {
    private MessengerModel model;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private String login;
    private volatile boolean stop = false;

    public UpdateList(MessengerModel model) {
        this.model = model;
        socket = model.getSocket();
        out = model.getOut();
        in = model.getIn();
        login = model.getLogin();
    }

    private boolean isSocketClosed() {
        return socket.isClosed();
    }

    @Override
    public void run() {
        updateOnlineList();
        updateChatMessages();
    }

    void updateChatMessages() {
        if (!model.getController().getSelectedUser().isPresent()) {
            return;
        } else {
            String user2 = model.getController().getSelectedUser().get();
            try {
                out.write("GET" + "\n");
                out.write(login + "\n");
                out.write(user2 + "\n");
                out.flush();
                StringBuilder answer = new StringBuilder();
                String line = in.readLine();
                while (!line.equals("END")) {
                    answer.append(line);
                    line = in.readLine();
                }
                String lastList = model.getChatMessages().getOrDefault(user2, "");
                String newList;
                newList = MessageParser.displayInChat(answer.toString());
                model.getChatMessages().put(user2, newList);
                if (!lastList.equals(newList)) {
                    Platform.runLater(() -> {
                        model.getController().showChatMessages(newList);
                    });
                    log.debug("List of messages changed: {}", answer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateOnlineList() {
        try {
            Optional<String> lastSelection = model.getController().getSelectedUser();
            model.getOut().write("ONLINE" + "\n");
            out.write(login + "\n");
            out.flush();
            StringBuilder answer = new StringBuilder();
            String line = in.readLine();
            while (!line.equals("END")) {
                answer.append(line);
                line = in.readLine();
            }
            List<String> lastList = model.getOnlineUsers();
            List<String> newList = MessageParser.getUsers(answer.toString());
            if (!lastList.equals(newList)) {
                Platform.runLater(() -> {
                    model.setOnlineUsers(newList);
                    model.getController().showOnline(model.getObservableList(), lastSelection);
                });
                log.debug("List of online users changed: {}", answer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
