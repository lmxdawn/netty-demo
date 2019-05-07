package com.example.nettydemo.websocket3;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 协议解码处理器，判断是什么协议（WebSocket还是TcpSocket）,然后动态修改编解码器
        pipeline.addLast("protocolHandler", new ProtocolDecoder());

        /** TcpSocket协议需要使用的编解码器 */
//        pipeline.addLast("tcpFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//        pipeline.addLast("tcpLengthFieldPrepender", new LengthFieldPrepender(4));
//        //字符串解码
//        pipeline.addLast("tcpStringDecoder", new StringDecoder(CharsetUtil.UTF_8));
//        //字符串编码
//        pipeline.addLast("tcpStringEncoder", new StringEncoder(CharsetUtil.UTF_8));

        /** WebSocket协议需要使用的编解码器 */
//        // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
//        pipeline.addLast("httpCodec", new HttpServerCodec());
//        // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
//        pipeline.addLast("httpAggregator", new HttpObjectAggregator(65536));
//        // 用于向客户端发送Html5文件，主要用于支持浏览器和服务端进行WebSocket通信
//        pipeline.addLast("httpChunked", new ChunkedWriteHandler());

        // websocket定义了传递数据的6中frame类型
        pipeline.addLast(new ServerFrameHandler());
        
    }
}
