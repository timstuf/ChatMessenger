package com.nure;

import com.nure.server.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
        try {// Read file fxml and draw interface.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/server.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            new com.nure.server.Server(controller).start();
            //Show scene 2 in new window
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Server");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
