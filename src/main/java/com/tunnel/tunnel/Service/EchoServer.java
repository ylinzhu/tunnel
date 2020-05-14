package com.tunnel.tunnel.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println("Usage:" + EchoServer.class.getSimpleName());
        return;
        }
         int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    private void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                 .channel(NioServerSocketChannel.class)
                 .localAddress(port)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline().addLast(new EchoServerHandler());
                     }
                 });
        ChannelFuture future = bootstrap.bind().sync();
        System.out.println(EchoServer.class.getName() + "started and listen on" + future.channel().localAddress());
        future.channel().closeFuture().sync();
        group.shutdownGracefully().sync();
    }
}
