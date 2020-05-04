package com.nure.server;

import com.nure.database.repositories.impl.MessageRepository;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.domain.Message;
import com.nure.parsers.MessageBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Server {
    private static final int PORT = 3434;
    private static volatile boolean stop = false;
    private static final int TIMEOUT = 500;
    private static Map<Long, Message> messages = new ConcurrentHashMap<>();
    private static AtomicInteger id = new AtomicInteger(0);
    private static UserRepository userRepository = UserRepository.getInstance();

    public static void main(String[] args) throws IOException, InterruptedException {
        loadPreviousMessages();
        quitCommandThread();
        ServerSocket serverSocket = new ServerSocket(PORT);
        log.info("started on {}", PORT);
        while (!stop) {
            Thread.sleep(100);
            //serverSocket.setSoTimeout(TIMEOUT);
            Socket socket;

            try {
                socket = serverSocket.accept();
                try {
                    new ServerThread(socket, id, messages);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            } catch (SocketTimeoutException e) {
                log.error(e.getMessage());
            }
        }
        log.info("Server stopped");
        userRepository.logEveryoneOut();
    }

    private static void loadPreviousMessages() {
        List<Message> previousMessages = MessageBuilder.loadAllPreviousMessagesInList();
        for (Message message : previousMessages) {
            messages.put(message.getId(), message);
        }
    }

    private static void quitCommandThread() {
        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String buf;
                try {
                    buf = reader.readLine();
                    if (buf.equals("quit")) {
                        stop = true;
                        break;
                    } else
                        log.warn("Type 'quit' for server termination");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }).start();
    }
}
