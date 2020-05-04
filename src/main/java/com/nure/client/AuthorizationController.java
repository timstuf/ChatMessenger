package com.nure.client;

import com.nure.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
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
        //if (login.getText().equals("") || password.getText().equals(""))
        // success.setText("Please input login and password");

        if (model == null) model = new Model(Constants.IP);

        //model.logIn(login.getText(), password.getText());
        model.logIn("adm", "adm");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent event) {
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");
    }
}
