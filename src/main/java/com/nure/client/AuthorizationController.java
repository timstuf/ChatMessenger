package com.nure.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class AuthorizationController implements Initializable {
    @FXML
    public PasswordField password;
    @FXML
    public TextField ip;
    private AuthorizationModel model;
    @FXML
    private Label error;
    @FXML
    private TextField login;

    private boolean isInputInvalid() {
        if (login.getText().equals("") || password.getText().equals("") || ip.getText().equals("")) {
            error.setText("Please fill all fields");
            return true;
        } else return false;
    }

    private void closeWindow() {
        Stage stage = (Stage) ip.getScene().getWindow();
        stage.close();
    }

    public void logIn(ActionEvent event) {
        if (isInputInvalid()) return;
        if (model == null) model = new AuthorizationModel();
        String answer = model.logIn(ip.getText(), login.getText(), password.getText());
        if (answer.equals("")) {
            closeWindow();
        } else error.setText(answer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent event) {
        if (isInputInvalid()) return;
        if (model == null) model = new AuthorizationModel();
        String answer = model.registerUser(ip.getText(), login.getText(), password.getText());
        if (answer.equals("")) {
            closeWindow();
        } else error.setText(answer);
    }
}
