package com.nure.server;

import com.nure.database.repositories.impl.ChatRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Chat;
import com.nure.domain.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.nure.util.Constants.PORT;

@Slf4j
public class Server {
    private static volatile boolean stop = false;
    private static final int TIMEOUT = 500;
    private static Map<Chat, List<Message>> messages = new ConcurrentHashMap<>();
    private static UserRepository userRepository = UserRepository.getInstance();
    private static ClientList clientList = ClientList.getInstance();
    private static ChatRepository chatRepository = ChatRepository.getInstance();

    public static void main(String[] args) throws IOException {
        loadPreviousMessages();
        quitCommandThread();
        ServerSocket serverSocket = new ServerSocket(PORT);
        log.info("started on {}", PORT);
        while (!stop) {
            serverSocket.setSoTimeout(TIMEOUT);
            Socket socket;

            try {
                socket = serverSocket.accept();
                try {
                    new ServerThread(socket, messages, clientList);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    socket.close();
                }
            } catch (SocketTimeoutException ignored) {
            }
        }
        log.info("Server stopped");
        userRepository.logEveryoneOut();
        saveAllNewMessages();
    }

    private static void saveAllNewMessages() {
        chatRepository.saveAllNewMessages(messages);
    }

    private static void loadPreviousMessages() {
        messages.putAll(chatRepository.getAllMessagesInAllChats());
    }

    private static void quitCommandThread() {
        new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String buf;
                try {
                    buf = br.readLine();
                    if ("quit".equals(buf)) {
                        stop = true;
                        break;
                    } else {
                        log.warn("Type 'quit' for exit termination");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
