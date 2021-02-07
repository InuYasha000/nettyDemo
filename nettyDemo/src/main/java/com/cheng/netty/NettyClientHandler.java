package com.cheng.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Auther: cheng
 * @Date: 2021/2/6 07:03
 * @Description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf requestBuffer;

    public NettyClientHandler() {
        byte[] requestBytes = "你好，我发送第一个请求".getBytes();
        requestBuffer = Unpooled.buffer(requestBytes.length);
        requestBuffer.writeBytes(requestBytes);
    }

    //网络连接建立好了
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接建立好写过去就行了
        ctx.writeAndFlush(requestBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf responseBuffer = (ByteBuf) msg;
        byte[] responseBytes = new byte[responseBuffer.readableBytes()];
        responseBuffer.readBytes(responseBytes);

        String response = new String(responseBytes,"UTF-8");
        System.out.println("接收到服务端的响应："+response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
