package com.nure.server;

import java.util.ArrayList;
import java.util.List;

public class ClientList {
    private static volatile ClientList clientList;
    private List<String> userNames = new ArrayList<>();

    private ClientList() {
    }

    public static ClientList getInstance() {
        ClientList local = clientList;
        if (local == null)
            synchronized (ClientList.class) {
                local = clientList;
                if (local == null) {
                    clientList = local = new ClientList();
                }
            }
        return local;
    }

    public void addName(String name) {
        userNames.add(name);
    }

    public void removeName(String name) {
        userNames.remove(name);
    }

    public String getOnlineExcept(String login) {
        List<String> list = new ArrayList<>(userNames);
        list.remove(login);
        return list.toString();
    }

    public boolean сontainsName(String name) {
        return userNames.contains(name);
    }
}
