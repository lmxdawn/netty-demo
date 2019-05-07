package com.example.nettydemo.websocket3;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

/**
 * 协议解码处理器，用来判断使用的什么协议（本例是指WebSocket还是TcpSocket），从而动态修改编解码器
 *
 * @author lucher
 *
 */
public class ProtocolDecoder extends ByteToMessageDecoder {

    /**
     * 请求行信息的长度，ws为：GET /ws HTTP/1.1， Http为：GET / HTTP/1.1
     */
    private static final int PROTOCOL_LENGTH = 16;
    /**
     * WebSocket握手协议的前缀， 本例限定为：GET /ws ，在访问ws的时候，请求地址需要为如下格式 ws://ip:port/ws
     */
    private static final String WEBSOCKET_PREFIX = "GET /ws";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String protocol = getBufStart(in);
        ChannelPipeline pipeline = ctx.channel().pipeline();
		System.out.println("ProtocolHandler protocol:" + protocol);
        if (protocol.startsWith(WEBSOCKET_PREFIX)) {
            // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
            pipeline.addLast(new HttpServerCodec());
            // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
            pipeline.addLast(new HttpObjectAggregator(65536));
            // 用于向客户端发送Html5文件，主要用于支持浏览器和服务端进行WebSocket通信
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new WebSocketServerCompressionHandler());
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
        } else {
            // TcpSocket协议处理
            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
            pipeline.addLast(new LengthFieldPrepender(4));
            //字符串解码
            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
            //字符串编码
            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        }
        // 重置index标记位
        in.resetReaderIndex();
        // 移除该协议处理器，该channel后续的处理由对应协议安排好的编解码器处理
        pipeline.remove(this);
    }

    /**
     * 获取buffer中指定长度的信息
     *
     * @param in
     * @return
     */
    private String getBufStart(ByteBuf in) {
        int length = in.readableBytes();
        if (length > PROTOCOL_LENGTH) {
            length = PROTOCOL_LENGTH;
        }
        // 标记读取位置
        in.markReaderIndex();
        byte[] content = new byte[length];
        in.readBytes(content);
        return new String(content);
    }
}
