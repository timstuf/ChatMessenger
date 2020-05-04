package com.nure.client;

import com.nure.database.repositories.impl.MessageRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Chat;
import com.nure.domain.User;
import com.nure.exceptions.NoUserException;
import com.nure.parsers.MessageBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MessengerController implements Initializable {
    @FXML
    public Label textMessages;
    private MessengerModel model;
    private UserRepository userRepository = UserRepository.getInstance();
    private MessageRepository messageRepository = MessageRepository.getInstance();
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
    void getUser(String name){
        model = new MessengerModel(userRepository.getUserByLogin(name).orElseThrow(()->new NoUserException(name)));
        this.name.setText(model.getUserLogin());

        chatList.setItems(observableList);
    }
    public void showChatMessages(MouseEvent mouseEvent) {
        User user2 = User.asObject(chatList.getSelectionModel().getSelectedItem());
        Chat chat = new Chat(user2, user);
        String messages = MessageBuilder.createDocument(messageRepository.getAllMessagesInChat(chat));
        textMessages.setText(messages);
    }
}
