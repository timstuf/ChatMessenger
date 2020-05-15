package com.nure.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Chat {

    private User user1;
    private User user2;

    public boolean isUserInChat(User user) {
        return user1 == user || user2 == user;
    }

    public static Chat asObject(String chat) {
        String[] logins = chat.split(" --- ");
        User user1 = new User(logins[0]);
        User user2 = new User(logins[1]);
        return new Chat(user1, user2);
    }

    public String invertChat() {
        String chat = toString();
        String[] logins = chat.split(" --- ");
        return logins[1] + " --- " + logins[0];
    }

    @Override
    public String toString() {
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
        int result = 17;
        result = 31 * result + user1.hashCode() + user2.hashCode();
        return result;
    }
}
