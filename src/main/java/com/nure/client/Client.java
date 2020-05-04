package com.nure.client;

import com.nure.util.Constants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {



        try {
            Socket socket = new Socket("localhost", 3434);
            BufferedWriter out =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("HELLO");
            out.flush();
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
