package com.nure.client;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MessengerController implements Initializable {
    @FXML
    public ScrollPane textMessages;
    @FXML
    public Button logOut;
    @FXML
    public Label name;
    @FXML
    public ListView<String> chatList;
    @FXML
    public TextField text;
    @FXML
    public Button send;
    private MessengerModel model;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void setModel(String name, Socket socket) {
        model = new MessengerModel(name, socket, this);
        this.name.setText(name);
        chatList.setItems(model.showOnline());
    }

    public Optional<String> getSelectedUser() {
        return Optional.ofNullable(chatList.getSelectionModel().getSelectedItem());
    }
    public void showChatMessages(MouseEvent mouseEvent) {
        String user2 = chatList.getSelectionModel().getSelectedItem();
        Text text = new Text(model.showChatMessages(user2));
        text.setWrappingWidth(textMessages.getWidth());
        textMessages.setContent(text);
    }

    public void showChatMessages(String messages) {
        Text label = new Text(messages);
        label.setWrappingWidth(textMessages.getWidth());
        textMessages.setContent(label);
    }

    public void showOnline(ObservableList<String> list, Optional<String> selection) {
        chatList.setItems(list);
        selection.ifPresent(s -> chatList.getSelectionModel().select(s));
    }


    public void sendMessage(ActionEvent event) {
        if (text.getText().equals("")) return;
        model.sendMessage(text.getText(), name.getText(), chatList.getSelectionModel().getSelectedItem());
        text.setText("");
    }

    public void logOut(ActionEvent event) {
        model.logOut();
        Stage stage = (Stage) text.getScene().getWindow();
        stage.close();
    }
}
