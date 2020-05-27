package com.nure.server;

import com.nure.domain.Chat;
import com.nure.domain.Message;
import com.nure.parsers.MessageBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Controller implements Initializable {

    @FXML
    public ScrollPane textMessages;
    @FXML
    private ListView<String> chatList;
    private Map<String, String> messagesInChat = new ConcurrentHashMap<>();
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textMessages.setFitToWidth(true);
    }

    public void showChatMessages(MouseEvent mouseEvent) {
        String selected = chatList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Chat chat = Chat.asObject(selected);
        String chatStr = chat.toString();
        if (!messagesInChat.containsKey(chatStr)) {
            chatStr = chat.invertChat();
        }
        String messages = messagesInChat.get(chatStr);
        Text label = new Text(messages);
        label.setWrappingWidth(textMessages.getWidth());
        textMessages.setContent(label);
    }

    public void setMessages(Map<Chat, List<Message>> messagesMap) {
        for (Map.Entry<Chat, List<Message>> entry : messagesMap.entrySet()) {
            messagesInChat.put(entry.getKey().toString(), MessageBuilder.showPrettyInChat(entry.getValue()));
        }
    }

    public void showOnline(Set<Chat> chats) {
        String selected = chatList.getSelectionModel().getSelectedItem();
        String chatsString = chats.toString();
        String[] list = chatsString.substring(1, chatsString.length() - 1).split(", ");
        observableList.setAll(Arrays.asList(list));
        chatList.setItems(observableList);
        if (selected != null) {
            chatList.getSelectionModel().select(selected);
        }
    }

    public void addMessageToChat(Chat chat, String messages) {
        String chatStr = chat.toString();
        if (!messagesInChat.containsKey(chatStr)) {
            chatStr = chat.invertChat();
            if (!messagesInChat.containsKey(chatStr)) {
                messagesInChat.put(chatStr, "");
            }
        }
        String previousMes = messagesInChat.get(chatStr);
        previousMes += messages;
        messagesInChat.put(chatStr, previousMes);
        showChatMessages(null);
    }
}
