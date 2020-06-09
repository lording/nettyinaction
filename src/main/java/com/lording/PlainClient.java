package com.lording;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PlainClient {
    public static void main(String[] args) throws IOException {
        var socket = new Socket("localhost", 8888);
        var in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
        while (in.hasNext()) {
            String line = in.nextLine();
            System.out.println("client receive: " + line);
        }
    }
}
