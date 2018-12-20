package com.example.nettydemo.websocket2;

import com.example.nettydemo.protobuf.MessageData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;

//处理文本协议数据，处理TextWebSocketFrame类型的数据，websocket专门处理文本的frame就是TextWebSocketFrame
public class ServerFrameHandler extends SimpleChannelInboundHandler<MessageData.RequestUser> {
    
    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    
    //读到客户端的内容并且向客户端去写内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageData.RequestUser msg) throws Exception {
        // channelGroup.add();
    
        Channel channel = ctx.channel();
        System.out.println(msg.getUserName());
        System.out.println(msg.getAge());
        System.out.println(msg.getPassword());
        MessageData.ResponseUser bank = MessageData
                .ResponseUser.newBuilder()
                .setUserName("你好,请问有什么可以帮助你!")
                .setAge(18).setPassword("11111").build();

        channel.writeAndFlush(bank);
    }
    
    //每个channel都有一个唯一的id值
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //打印出channel唯一值，asLongText方法是channel的id的全名
        // System.out.println("handlerAdded："+ctx.channel().id().asLongText());
    }
    
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.close();
    }
    
}
