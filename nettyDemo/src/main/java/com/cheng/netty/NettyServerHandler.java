package com.cheng.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: cheng
 * @Date: 2021/2/6 06:10
 * @Description:
 */
public class NettyServerHandler extends ChannelHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] requestByte = new byte[buf.readableBytes()];
//        buf.readBytes(requestByte);
//
//        String requestData = new String(requestByte, "UTF-8");

        //因为前面是转换成了字符串，这里直接使用字符串就行了
        String request = (String )msg;
        System.out.println("接收到的请求：" + request);

//        String response = "收到你的消息了，返回响应给你";
        String response = "收到你的消息了，返回响应给你"+"$_";
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuffer);

        //这个东西类似对应着那个processor线程，负责读取请求，返回响应
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
