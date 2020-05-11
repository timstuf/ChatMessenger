package com.nure.client.timers;

import com.nure.client.MessengerModel;
import com.nure.parsers.MessageParser;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

@Slf4j
public class UpdateOnlineList extends TimerTask {
    private MessengerModel model;
    private BufferedWriter out;
    private BufferedReader in;
    private String login;

    public UpdateOnlineList(MessengerModel model) {
        this.model = model;
        out = model.getOut();
        in = model.getIn();
        login = model.getLogin();
    }

    @Override
    public void run() {
        updateOnlineList();
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
}
