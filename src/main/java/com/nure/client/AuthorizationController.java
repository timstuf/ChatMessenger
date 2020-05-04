package com.nure.client;

import com.nure.database.repositories.impl.UserRepository;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class AuthorizationController implements Initializable {
    @FXML
    public TextField password;
    private UserRepository userRepository = UserRepository.getInstance();
    @FXML
    private Label success;
    @FXML
    private TextField login;

    public void showLogin(ActionEvent event) {
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");
        String bool = userRepository.tryLogin(login.getText(), password.getText());
        if(bool.equals("")) try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            MessengerController scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.getUser(login.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Second Window");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent event) {
        if (login.getText().equals("") || password.getText().equals(""))
            success.setText("Please input login and password");
    }
}
