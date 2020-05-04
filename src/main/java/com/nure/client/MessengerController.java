package com.nure.client;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MessengerController implements Initializable {
    @FXML
    public Label textMessages;
    private MessengerModel model;
    private UserRepository userRepository = UserRepository.getInstance();
    @FXML
    public Label name;
    @FXML
    public ListView<String> chatList;

    @FXML
    public TextField text;
    @FXML
    public Button send;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void setModel(String name, Socket socket) {
        model = new MessengerModel(name, socket);
        this.name.setText(name);
        chatList.setItems(model.showOnline());
    }
    public void showChatMessages(MouseEvent mouseEvent) {
        User user2 = User.asObject(chatList.getSelectionModel().getSelectedItem());
        //Chat chat = new Chat(user2, user);
        String messages ="";// MessageBuilder.createDocument(messageRepository.getAllMessagesInChat(chat));
        textMessages.setText(messages);
    }

    //TODO: show in chat after
    public void sendMessage(ActionEvent event) {
        if (text.getText().equals("")) return;
        model.sendMessage(text.getText(), name.getText(), chatList.getSelectionModel().getSelectedItem());

    }
}
