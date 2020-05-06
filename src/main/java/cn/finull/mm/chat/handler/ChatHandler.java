package cn.finull.mm.chat.handler;

import cn.finull.mm.chat.entity.ReqEntity;
import cn.finull.mm.chat.service.ServiceFactory;
import cn.finull.mm.chat.util.ChannelGroupUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Description
 * <p> 聊天处理
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 12:10
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<ReqEntity> {

    /**
     * 心跳检测
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                ctx.close();
            }
        }
    }

    /**
     * 连接建立
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ChannelGroupUtil.addChannel(ctx.channel());
    }

    /**
     * 处理消息
     * @param channelHandlerContext
     * @param reqEntity
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ReqEntity reqEntity) throws Exception {
        // 处理消息
        ServiceFactory.buildChatService(reqEntity.getMsgType()).handle(channelHandlerContext.channel(), reqEntity);
    }

    /**
     * 连接断开
     * close()后会调用此方法
     * 当前channel已从ChannelGroup中被移除，不需要再手动移除
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Long userId = ChannelGroupUtil.remove(ctx.channel());
        log.info("用户[{}]断开连接，剩余用户[{}]个！", userId, ChannelGroupUtil.channelSize());
    }

    /**
     * 处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error("聊天通道异常！", cause);
    }
}
