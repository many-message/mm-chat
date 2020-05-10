package cn.finull.mm.chat.service;

import cn.finull.mm.chat.entity.ReqEntity;
import cn.finull.mm.chat.entity.RespEntity;
import cn.finull.mm.chat.entity.enums.MsgTypeEnum;
import cn.finull.mm.chat.entity.req.*;
import cn.finull.mm.chat.entity.resp.*;
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
        }
        // 异步添加一条消息
        httpService.addMsg(reqEntity.getSendUserId(), inboundPrivateMsg, () -> { });
    }

    /**
     * 好友请求通知
     * @param channel
     * @param reqEntity
     */
    public void handleFriendReq(Channel channel, ReqEntity reqEntity) {
        InboundFriendReq inboundFriendReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundFriendReq.class);

        OutboundFriendReq outboundFriendReq = new OutboundFriendReq(inboundFriendReq.getNickname());

        ChannelGroupUtil.writeAndFlush(
                List.of(inboundFriendReq.getRecvUserId()),
                new RespEntity(MsgTypeEnum.FRIEND_REQ_NOTICE, JsonUtil.toJSONString(outboundFriendReq)));
    }

    /**
     * 处理删除好友通知
     * @param channel
     * @param reqEntity
     */
    public void handleDelFriend(Channel channel, ReqEntity reqEntity) {
        InboundDelFriend inboundDelFriend = JsonUtil.parseObject(
                reqEntity.getContent(), InboundDelFriend.class);

        OutboundDelFriend outboundDelFriend = new OutboundDelFriend(
                reqEntity.getSendUserId(), inboundDelFriend.getNickname());

        ChannelGroupUtil.writeAndFlush(
                List.of(inboundDelFriend.getRecvUserId()),
                new RespEntity(MsgTypeEnum.DEL_FRIEND_NOTICE, JsonUtil.toJSONString(outboundDelFriend))
        );
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
        // groupId, sendGroupMemberId, groupMemberName, nickname, msg, addition
        BeanUtil.copyProperties(inboundGroupMsg, outboundGroupMsg);
        outboundGroupMsg.setSendUserId(reqEntity.getSendUserId());
        outboundGroupMsg.setCreateTime(new Date());

        // 不给自己发
        inboundGroupMsg.getRecvUserIds().removeIf(reqEntity.getSendUserId()::equals);

        ChannelGroupUtil.writeAndFlush(inboundGroupMsg.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.GROUP_CHAT, JsonUtil.toJSONString(outboundGroupMsg)));

        // 存数据库
        httpService.addGroupMsg(reqEntity.getSendUserId(), inboundGroupMsg, () -> {});
    }

    /**
     * 处理入群请求通知
     * @param channel
     * @param reqEntity
     */
    public void handleReqJoinGroup(Channel channel, ReqEntity reqEntity) {
        InboundGroupJoinReq inboundGroupJoinReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundGroupJoinReq.class);

        OutboundGroupJoinReq outboundGroupJoinReq = new OutboundGroupJoinReq();
        BeanUtil.copyProperties(inboundGroupJoinReq, outboundGroupJoinReq);

        ChannelGroupUtil.writeAndFlush(
                inboundGroupJoinReq.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.REQ_JOIN_GROUP_NOTICE, JsonUtil.toJSONString(outboundGroupJoinReq)));
    }

    /**
     * 处理入群邀请通知
     * @param channel
     * @param reqEntity
     */
    public void handleInviteJoinGroup(Channel channel, ReqEntity reqEntity) {
        InboundGroupInviteReq inboundGroupInviteReq = JsonUtil.parseObject(
                reqEntity.getContent(), InboundGroupInviteReq.class);

        OutboundGroupJoinInvite groupJoinInvite = new OutboundGroupJoinInvite();
        BeanUtil.copyProperties(inboundGroupInviteReq, groupJoinInvite);

        ChannelGroupUtil.writeAndFlush(
                inboundGroupInviteReq.getInviteUserIds(),
                new RespEntity(MsgTypeEnum.INVITE_JOIN_GROUP_NOTICE, JsonUtil.toJSONString(groupJoinInvite)));
    }

    /**
     * 处理删除群成员通知
     * @param channel
     * @param reqEntity
     */
    public void handleDelGroupMember(Channel channel, ReqEntity reqEntity) {
        InboundDelGroupMember inboundDelGroupMember = JsonUtil.parseObject(
                reqEntity.getContent(), InboundDelGroupMember.class);

        OutboundDelGroupMember outboundDelGroupMember = new OutboundDelGroupMember();
        BeanUtil.copyProperties(inboundDelGroupMember, outboundDelGroupMember);

        ChannelGroupUtil.writeAndFlush(
                List.of(inboundDelGroupMember.getRecvUserId()),
                new RespEntity(MsgTypeEnum.DEL_GROUP_MEMBER_NOTICE, JsonUtil.toJSONString(outboundDelGroupMember)));
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

        OutboundDelGroup outboundDelGroup = new OutboundDelGroup();
        // groupId, nickname, groupName
        BeanUtil.copyProperties(inboundDelGroup, outboundDelGroup);

        ChannelGroupUtil.writeAndFlush(
                inboundDelGroup.getRecvUserIds(),
                new RespEntity(MsgTypeEnum.DEL_GROUP_NOTICE, JsonUtil.toJSONString(outboundDelGroup)));
    }
}
