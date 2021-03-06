package com.nure.client;

import com.nure.util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;


@Slf4j
public class AuthorizationModel {
    private static BufferedWriter out;
    private static BufferedReader in;
    private String login;
    private Socket socket;

    private void connectToServer(String server) throws IOException {
        socket = new Socket(server, Constants.PORT);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    String logIn(String ip, String login, String password) {
        try {
            connectToServer(ip);
        } catch (IOException e) {
            return "Cannot connect to this server";
        }
        // if (!ip.equals(Constants.IP)) return "Cannot connect to this server";
        String answer = "";
        try {
            out.write("CONNECT"+ "\n");
            out.write(login+ "\n");
            out.write(password+ "\n");
            out.flush();
            answer = in.readLine();
            log.debug("answer: {}", answer);
            if(answer.equals("OK")) {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.fxml"));
                Parent root = loader.load();

                //Get controller of scene2
                MessengerController scene2Controller = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                scene2Controller.setModel(login, socket);

                //Show scene 2 in new window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Chat");
                stage.show();
                answer = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    String registerUser(String ip, String login, String password) {
        try {
            connectToServer(ip);
        } catch (IOException e) {
            return "Cannot connect to this server";
        }
        //if (!ip.equals(Constants.IP)) return "Cannot connect to this server";
        String answer = "";
        try {
            out.write("NEW" + "\n");
            out.write(login + "\n");
            out.write(password + "\n");
            out.flush();
            answer = in.readLine();
            log.debug("answer: {}", answer);
            if (answer.equals("OK")) {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.fxml"));
                Parent root = loader.load();

                //Get controller of scene2
                MessengerController scene2Controller = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                scene2Controller.setModel(login, socket);

                //Show scene 2 in new window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Second Window");
                stage.show();
                answer = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }
}
