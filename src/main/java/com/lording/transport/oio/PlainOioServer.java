package com.lording.transport.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
    public void server(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port); //将服务器绑定到指定端口

        for (; ; ) {
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            //创建一个新的线程来处理该链接
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream out = clientSocket.getOutputStream();
                        //将消息写给已链接的客户端
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                        clientSocket.shutdownOutput();
                        //clientSocket.close();   //关闭连接
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start(); //启动线程
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8888;
        new PlainOioServer().server(port);
    }
}
