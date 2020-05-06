package com.nure.client.timers;

import com.nure.client.MessengerModel;
import com.nure.parsers.MessageParser;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

//TODO: when updating online list make sure selection stays the same
@Slf4j
public class UpdateList extends TimerTask {
    private MessengerModel model;
    private BufferedWriter out;
    private BufferedReader in;
    private String login;

    public UpdateList(MessengerModel model) {
        this.model = model;
        out = model.getOut();
        in = model.getIn();
        login = model.getLogin();
    }

    @Override
    public void run() {
        updateOnlineList();
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
                String lastList = model.getChatMessages().get(user2);
                String newList;
                newList = MessageParser.displayInChat(answer.toString());
                model.getChatMessages().put(user2, newList);
                if (!lastList.equals(newList)) {
                    model.getController().showChatMessages(newList);
                    log.debug("List of messages changed: {}", answer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateOnlineList() {
        try {
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
            model.setOnlineUsers(newList);
            if (!lastList.equals(newList)) {
                model.getController().showOnline(model.getObservableList());
                log.debug("List of online users changed: {}", answer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
