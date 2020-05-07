package cn.finull.mm.chat.service;

import cn.finull.mm.chat.entity.ReqEntity;
import cn.finull.mm.chat.entity.RespEntity;
import cn.finull.mm.chat.entity.enums.MsgTypeEnum;
import cn.finull.mm.chat.entity.req.*;
import cn.finull.mm.chat.entity.resp.OutboundGroupMsg;
import cn.finull.mm.chat.entity.resp.OutboundPrivateMsg;
import cn.finull.mm.chat.service.http.HttpService;
import cn.finull.mm.chat.util.ChannelGroupUtil;
import cn.finull.mm.chat.util.JsonUtil;
import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

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

    private final HttpService httpService = HttpService.build();

    /**
     * 处理连接
     * @param channel
     * @param reqEntity
     */
    public void handleConn(Channel channel, ReqEntity reqEntity) {
        ChannelGroupUtil.put(channel, reqEntity.getSendUserId());
        log.info("用户[{}]连接到服务器，当前用户[{}]个！", reqEntity.getSendUserId(), ChannelGroupUtil.channelSize());
    }

    /**
     * 处理心跳检测
     * @param channel
     * @param reqEntity
     */
    public void handleIdle(Channel channel, ReqEntity reqEntity) {
        RespEntity respEntity = new RespEntity();
        ChannelGroupUtil.put(channel, reqEntity.getSendUserId());
        BeanUtil.copyProperties(reqEntity, respEntity);
        channel.writeAndFlush(respEntity);
    }

    /**
     * 私聊
     * @param channel
     * @param reqEntity
     */
    public void handlePrivateChat(Channel channel, ReqEntity reqEntity) {
        InboundPrivateMsg inboundPrivateMsg = JsonUtil.parseObject(reqEntity.getContent(), InboundPrivateMsg.class);
        if (ChannelGroupUtil.isActive(inboundPrivateMsg.getRecvUserId())) {
            // 接收用户在线
            OutboundPrivateMsg outboundPrivateMsg = new OutboundPrivateMsg();
            BeanUtil.copyProperties(inboundPrivateMsg, outboundPrivateMsg);
            outboundPrivateMsg.setSendUserId(reqEntity.getSendUserId());
            outboundPrivateMsg.setCreateTime(new Date());
            // 向指定用户发送消息
            ChannelGroupUtil.writeAndFlush(List.of(inboundPrivateMsg.getRecvUserId()),
                    new RespEntity(MsgTypeEnum.PRIVATE_CHAT, JsonUtil.toJSONString(outboundPrivateMsg)));
        } else {
            // 接收用户不在线
            httpService.addMsg(reqEntity.getSendUserId(), inboundPrivateMsg, () -> { });
        }
    }

    /**
     * 好友请求通知
     * @param channel
     * @param reqEntity
     */
    public void handleFriendReq(Channel channel, ReqEntity reqEntity) {
        InboundFriendReq inboundFriendReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundFriendReq.class);
        ChannelGroupUtil.writeAndFlush(
                List.of(inboundFriendReq.getRecvUserId()),
                new RespEntity(MsgTypeEnum.FRIEND_REQ_NOTICE, null));
    }

    /**
     * 处理删除好友通知
     * @param channel
     * @param reqEntity
     */
    public void handleDelFriend(Channel channel, ReqEntity reqEntity) {
        InboundDelFriend inboundDelFriend = JsonUtil.parseObject(
                reqEntity.getContent(), InboundDelFriend.class);
        ChannelGroupUtil.writeAndFlush(
                List.of(inboundDelFriend.getRecvUserId()),
                new RespEntity(MsgTypeEnum.DEL_FRIEND_NOTICE, null));
    }

    /**
     * 处理群聊
     * @param channel
     * @param reqEntity
     */
    public void handleGroupChat(Channel channel, ReqEntity reqEntity) {
        InboundGroupMsg inboundGroupMsg = JsonUtil.parseObject(
                reqEntity.getContent(), InboundGroupMsg.class);

        OutboundGroupMsg outboundGroupMsg = new OutboundGroupMsg();
        // groupId, sendGroupMemberId, msg, addition
        BeanUtil.copyProperties(inboundGroupMsg, outboundGroupMsg);
        outboundGroupMsg.setCreateTime(new Date());

        // 不给自己发
        inboundGroupMsg.getRecvUserIds().removeIf(reqEntity.getSendUserId()::equals);

        ChannelGroupUtil.writeAndFlush(inboundGroupMsg.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.GROUP_CHAT, JsonUtil.toJSONString(outboundGroupMsg)));
    }

    /**
     * 处理入群请求通知
     * @param channel
     * @param reqEntity
     */
    public void handleReqJoinGroup(Channel channel, ReqEntity reqEntity) {
        InboundGroupJoinReq inboundGroupJoinReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundGroupJoinReq.class);
        ChannelGroupUtil.writeAndFlush(
                inboundGroupJoinReq.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.REQ_JOIN_GROUP_NOTICE, null));
    }

    /**
     * 处理入群邀请通知
     * @param channel
     * @param reqEntity
     */
    public void handleInviteJoinGroup(Channel channel, ReqEntity reqEntity) {
        InboundGroupInviteReq inboundGroupInviteReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundGroupInviteReq.class);
        ChannelGroupUtil.writeAndFlush(
                inboundGroupInviteReq.getInviteUserIds(),
                new RespEntity(MsgTypeEnum.INVITE_JOIN_GROUP_NOTICE, null));
    }

    /**
     * 处理删除群成员通知
     * @param channel
     * @param reqEntity
     */
    public void handleDelGroupMember(Channel channel, ReqEntity reqEntity) {
        InboundDelGroupMember inboundDelGroupMember = JsonUtil.parseObject(
                reqEntity.getContent(), InboundDelGroupMember.class);
        ChannelGroupUtil.writeAndFlush(
                List.of(inboundDelGroupMember.getRecvUserId()),
                new RespEntity(MsgTypeEnum.DEL_GROUP_MEMBER_NOTICE, null));
    }

    /**
     * 处理删除群通知
     * @param channel
     * @param reqEntity
     */
    public void handleDelGroup(Channel channel, ReqEntity reqEntity) {
        InboundDelGroup inboundDelGroup = JsonUtil.parseObject(
                reqEntity.getContent(), InboundDelGroup.class);

        // 不给自己发
        inboundDelGroup.getRecvUserIds().removeIf(reqEntity.getSendUserId()::equals);

        ChannelGroupUtil.writeAndFlush(
                inboundDelGroup.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.DEL_GROUP_NOTICE, null));
    }
}
