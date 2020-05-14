package com.nure;

import com.nure.server.Controller;
import com.nure.server.Server;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launchServerBefore();
        launch();
    }

    private static void launchServerBefore() {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/server.fxml"));
        try {
            Parent root = loader.load();
            //Get controller of scene2
            Controller controller = loader.getController();
            new Server(controller).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {// Read file fxml and draw interface.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/server.fxml"));
            Parent root = loader.load();
            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Server");
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
