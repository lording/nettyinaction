package com.lording.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int m_port;

    public EchoServer(int m_port) {
        this.m_port = m_port;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uasge: " + EchoServer.class.getName() + "<port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        EventLoopGroup group = new NioEventLoopGroup(); //创建EventLoopEvent

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();    //创建ServerBoostrap
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)      //指定使用NIO传输Channel
                    .localAddress(new InetSocketAddress(m_port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

                ChannelFuture channelFuture = serverBootstrap.bind().sync();    //异步地绑定服务器，调用sync方法阻塞等待直到绑定完成
                channelFuture.channel().closeFuture().sync();   //获取Channel的CLoseFuture，并且阻塞当前线程直到它完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();  //关闭EventLoopGroup释放所有的资源
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
