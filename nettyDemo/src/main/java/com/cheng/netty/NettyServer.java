package com.cheng.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
                            //收到换行符就表示收到了完整数据
                            //按照换行符截取一条条数据
//                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));

                            //也可以自定义符号
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            //1024表示解析了1024字节的长度后还是没有解析完，那么就报错
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));

                            //然后转换成字符串
                            socketChannel.pipeline().addLast(new StringDecoder());
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
