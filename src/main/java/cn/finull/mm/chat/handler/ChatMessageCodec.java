package cn.finull.mm.chat.handler;

import cn.finull.mm.chat.entity.ReqEntity;
import cn.finull.mm.chat.entity.RespEntity;
import cn.finull.mm.chat.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * Description
 * <p> 自定义消息编解码器
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 12:01
 */
public class ChatMessageCodec extends MessageToMessageCodec<TextWebSocketFrame, RespEntity> {

    /**
     * 出站
     * RespEntity -> TextWebSocketFrame
     * @param channelHandlerContext
     * @param respEntity
     * @param list
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RespEntity respEntity, List<Object> list) throws Exception {
        String text = JsonUtil.toJSONString(respEntity);
        list.add(new TextWebSocketFrame(text));
    }

    /**
     * 入站
     * TextWebSocketFrame -> ReqEntity
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        String text = textWebSocketFrame.text();
        ReqEntity reqEntity = JsonUtil.parseObject(text, ReqEntity.class);
        list.add(reqEntity);
    }
}
