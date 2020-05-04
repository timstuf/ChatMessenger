package com.nure.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;

import java.io.IOException;
@NoArgsConstructor
public class AuthorizationView extends javafx.application.Application{
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        if (root != null)
            stage.setScene(new Scene(root));
        else throw new IllegalArgumentException();
        stage.setTitle("Second Window");
        stage.show();
    }
}
