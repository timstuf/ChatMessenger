package com.nure.server;

import com.nure.database.repositories.impl.ChatRepository;
import com.nure.database.repositories.impl.MessageRepository;
import com.nure.domain.Chat;
import com.nure.parsers.MessageBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    public Label textMessages;
    @FXML
    private ListView<String> chatList;
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private ChatRepository chatRepository = ChatRepository.getInstance();
    private MessageRepository messageRepository = MessageRepository.getInstance();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableList.addAll(chatRepository.getAllChats().stream().map(Chat::asChat).collect(Collectors.toList()));
        chatList.setItems(observableList);
    }

    public void showChatMessages(MouseEvent mouseEvent) {
        Chat chat = Chat.asObject(chatList.getSelectionModel().getSelectedItem());
        String messages = MessageBuilder.showPrettyInChat((messageRepository.getAllMessagesInChat(chat)));
        textMessages.setText(messages);
    }
}
