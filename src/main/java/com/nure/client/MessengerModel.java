package com.nure.client;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.OutputStream;
import java.util.EventListener;
import java.util.stream.Collectors;

public class MessengerModel implements EventListener {
    private User user;
    private UserRepository userRepository = UserRepository.getInstance();

    private ObservableList<String> observableList = FXCollections.observableArrayList();
    public MessengerModel(User user) {
        this.user = user;
        observableList.addAll(userRepository.findActiveUsersExceptOne(user)
                .stream().map(User::getLogin).collect(Collectors.toList()));
    }
    public String getUserLogin(){
        return user.getLogin();
    }
}
