package com.cheng.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @Date:2021/2/4 17:40
 * @Author: Cheng
 * @Description:netty服务端
 */
public class NettyServer {

    public static void main(String[] args) throws Exception{

        EventLoopGroup mainGroup = new NioEventLoopGroup();
    }

}
