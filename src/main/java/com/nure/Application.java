package com.nure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import static com.sun.activation.registries.LogSupport.log;


@Slf4j
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
        try {// Read file fxml and draw interface.
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/signin.fxml"));

            primaryStage.setTitle("My Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        UserRepository userRepository = new UserRepository();
//        MessageRepository messageRepository = new MessageRepository();
//        //new UserController().registrateUser("adm2", "adm2");
//        User user1 = userRepository.getUserById(1L);
//        User user2 = userRepository.getUserByLogin("adm2");
//        Message message = new Message(user1, user2, "from 1 to 2", LocalDateTime.now());
//        messageRepository.saveMessage(message);
        launch();
    }

}
