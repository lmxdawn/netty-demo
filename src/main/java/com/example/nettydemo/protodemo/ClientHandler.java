package com.example.nettydemo.protodemo;

import com.example.nettydemo.protobuf.MessageData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        if (msg instanceof MessageData.ResponseUser) {
            MessageData.ResponseUser responseBank = (MessageData.ResponseUser) msg;
            System.out.println(responseBank.getUserName());
            System.out.println(responseBank.getAge());
            System.out.println(responseBank.getPassword());
        }
        
    }
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageData.RequestUser user = MessageData.RequestUser.newBuilder()
                .setUserName("zhihao.miao").setAge(27).setPassword("123456").build();
        ctx.channel().writeAndFlush(user);
    
        System.out.println(user);
        
    }
}
