package com.nure.client.timers;

import com.nure.client.MessengerModel;
import com.nure.parsers.MessageParser;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TimerTask;

@Slf4j
public class UpdateMessageList extends TimerTask {
    private MessengerModel model;
    private BufferedWriter out;
    private BufferedReader in;
    private String login;

    public UpdateMessageList(MessengerModel model) {
        this.model = model;
        out = model.getOut();
        in = model.getIn();
        login = model.getLogin();
    }

    @Override
    public void run() {
        updateChatMessages();
    }

    void updateChatMessages() {
        if (!model.getController().getSelectedUser().isPresent()) {
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


}
