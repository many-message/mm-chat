package cn.finull.mm.chat.config;

import cn.finull.mm.chat.handler.ChatHandler;
import cn.finull.mm.chat.handler.ChatMessageCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Description
 * <p> 初始化所有handler
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-13 19:57
 */
public class HandlerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * http协议的最大长度
     */
    private static final int MAX_CONTENT_LENGTH = 8192;
    /**
     * 心跳检测的时间
     */
    private static final int IDLE_STATE_TIME = 1;
    /**
     * websocket协议的前缀
     */
    private static final String WEBSOCKET_PATH = "/ws";

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH))
                // WebSocket协议后缀为 /ws
                .addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH))
                // 自定义消息格式编解码器
                .addLast(new ChatMessageCodec())
                // 心跳检测时间为1分钟
                .addLast(new IdleStateHandler(IDLE_STATE_TIME, IDLE_STATE_TIME, IDLE_STATE_TIME, TimeUnit.MINUTES))
                // 聊天处理器
                .addLast(new ChatHandler());
    }
}
