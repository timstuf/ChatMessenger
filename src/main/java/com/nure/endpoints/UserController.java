package com.nure.endpoints;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.User;

public class UserController {
    private UserRepository userRepository = UserRepository.getInstance();
    public void registrateUser(String login, String password){
        userRepository.saveUser(new User(login, password));
    }
}
