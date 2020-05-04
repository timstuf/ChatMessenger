package com.nure.client;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

@Slf4j
public class AuthorizationController implements Initializable {
    @FXML
    public TextField password;
    private Model model;
    @FXML
    private Label success;
    @FXML
    private TextField login;


    public void showLogin(ActionEvent event) {
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");

        if (model == null) model = new Model(Constants.IP);

        model.logIn(login.getText(), password.getText());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent event) {
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");
    }
}
