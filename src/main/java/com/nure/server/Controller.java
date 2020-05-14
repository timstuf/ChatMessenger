package com.nure.server;

import com.nure.domain.Chat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Controller implements Initializable {
    @FXML
    public Label textMessages;
    @FXML
    private ListView<String> chatList;
    private Map<Chat, String> messagesInChat = new ConcurrentHashMap<>();
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showChatMessages(MouseEvent mouseEvent) {
        String selected = chatList.getSelectionModel().getSelectedItem();
        if (selected.equals("")) return;
        Chat chat = Chat.asObject(selected);
        String messages = messagesInChat.get(chat);
        textMessages.setText(messages);
    }

    public void showOnline(Set<Chat> chats) {
        observableList.setAll(chats.toString());
        chatList.setItems(observableList);
    }

    public void addMessageToChat(Chat chat, String messages) {
        String previousMes = messagesInChat.get(chat);
        previousMes += messages;
        messagesInChat.put(chat, previousMes);
    }
}
