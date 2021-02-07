package com.cheng.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Date:2021/2/4 17:40
 * @Author: Cheng
 * @Description:netty服务端
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {

        //线程组
        EventLoopGroup parentGroup = new NioEventLoopGroup();//Acceotor线程
        EventLoopGroup childGroup = new NioEventLoopGroup();//Processor，Handler

        try {
            //相当于服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)//用这个channel监听连接
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//代表处理每个客户端连接的socketChannel
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //处理网络连接
                            //链式调用
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });//最多有1024个请求排队

            //同步等待监听服务器监控端口
            ChannelFuture channelFuture = serverBootstrap.bind(50070).sync();
            //同步等待关闭启动服务器结果
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}
