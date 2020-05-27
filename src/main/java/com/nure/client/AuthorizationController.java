package com.nure.client;

import com.nure.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private Label success;
    @FXML
    private TextField login;


    public void showLogin(ActionEvent event) {
        if (model == null) model = new AuthorizationModel(Constants.IP);
        if (login.getText().equals("") && password.getText().equals(""))
            model.logIn(Constants.IP, "adm", "adm");
        else success.setText(model.logIn(ip.getText(), login.getText(), password.getText()));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //todo: add server valid
    public void registerUser(ActionEvent event) {
        if (model == null) model = new AuthorizationModel(Constants.IP);
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");
        else success.setText(model.registerUser(ip.getText(), login.getText(), password.getText()));
    }
}
