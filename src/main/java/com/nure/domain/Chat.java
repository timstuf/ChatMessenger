package com.nure.domain;

import com.nure.database.repositories.impl.UserRepository;
import com.nure.exceptions.NoUserException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class Chat {

    private User user1;

    private User user2;
    private static UserRepository userRepository = UserRepository.getInstance();

    public boolean isUserInChat(User user) {
        return user1 == user || user2 == user;
    }

    public static Chat asObject(String chat) {
        String[] logins = chat.split(" --- ");
        User user1 = userRepository.getUserByLogin(logins[0]).orElseThrow(() -> new NoUserException(logins[0]));
        User user2 = userRepository.getUserByLogin(logins[1]).orElseThrow(() -> new NoUserException(logins[1]));
        return new Chat(user1, user2);
    }

    public String asChat() {
        return user1.getLogin() + " --- " + user2.getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return (user1.equals(chat.user1) &&
                user2.equals(chat.user2)) || (user1.equals(chat.user2) &&
                user2.equals(chat.user1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
