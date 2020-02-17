package cn.finull.mm.chat.service;

import cn.finull.mm.chat.entity.ReqEntity;
import cn.finull.mm.chat.entity.RespEntity;
import cn.finull.mm.chat.util.ChannelGroupUtil;
import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 15:10
 */
@Slf4j
public class ChatService {

    /**
     * 处理连接
     * @param channel
     * @param reqEntity
     */
    public void handleConn(Channel channel, ReqEntity reqEntity) {
        ChannelGroupUtil.put(channel, reqEntity.getSendUserId());
        log.info("用户[{}]连接到服务器，当前用户[{}]！", reqEntity.getSendUserId(), ChannelGroupUtil.channelSize());
    }

    /**
     * 处理心跳检测
     * @param channel
     * @param reqEntity
     */
    public void handleIdle(Channel channel, ReqEntity reqEntity) {
        RespEntity respEntity = new RespEntity();
        BeanUtil.copyProperties(reqEntity, respEntity);
        channel.writeAndFlush(respEntity);
    }

    /**
     * 私聊
     * @param channel
     * @param reqEntity
     */
    public void handlePrivateChat(Channel channel, ReqEntity reqEntity) {

    }

    /**
     * 处理好友请求
     * @param channel
     * @param reqEntity
     */
    public void handleFriendReq(Channel channel, ReqEntity reqEntity) {

    }

    /**
     * 处理好友删除
     * @param channel
     * @param reqEntity
     */
    public void handleFriendDel(Channel channel, ReqEntity reqEntity) {

    }

    /**
     * 处理群聊
     * @param channel
     * @param reqEntity
     */
    public void handleGroupChat(Channel channel, ReqEntity reqEntity) {

    }

    /**
     * 处理入群请求
     * @param channel
     * @param reqEntity
     */
    public void handleReqJoinGroup(Channel channel, ReqEntity reqEntity) {

    }

    /**
     * 处理群加入邀请
     * @param channel
     * @param reqEntity
     */
    public void handleInviteJoinGroup(Channel channel, ReqEntity reqEntity) {

    }
}
