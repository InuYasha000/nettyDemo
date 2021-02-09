package com.cheng.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Auther: cheng
 * @Date: 2021/2/6 07:00
 * @Description:
 */
public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //启动类的意思
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());

//                    channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                    channel.pipeline().addLast(new StringDecoder());
                    channel.pipeline().addLast(new NettyClientHandler());
                }
            });

            //最终在AbstractNioChannel.AbstractNioUnsafe.connect
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",50070).sync();

            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
