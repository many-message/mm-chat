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
import java.util.stream.Collectors;

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
     * 处理好友请求
     * @param channel
     * @param reqEntity
     */
    public void handleFriendReq(Channel channel, ReqEntity reqEntity) {
        InboundFriendReq inboundFriendReq = JsonUtil.parseObject(reqEntity.getContent(), InboundFriendReq.class);
        httpService.addFriendReq(reqEntity.getSendUserId(), inboundFriendReq, () -> {
            ChannelGroupUtil.writeAndFlush(List.of(inboundFriendReq.getRecvUserId()), new RespEntity(MsgTypeEnum.FRIEND_REQ, null));
        });
    }

    /**
     * 处理好友请求状态修改
     * @param channel
     * @param reqEntity
     */
    public void handleFriendReqUpdate(Channel channel, ReqEntity reqEntity) {
        InboundFriendReqStatusUpdate inboundFriendReqStatusUpdate = JsonUtil.parseObject(reqEntity.getContent(), InboundFriendReqStatusUpdate.class);
        httpService.updateFriendReqStatus(inboundFriendReqStatusUpdate.getFriendReqId(),
                inboundFriendReqStatusUpdate.getFriendReqStatus(),
                () -> {
                    if ("2".equals(inboundFriendReqStatusUpdate.getFriendReqStatus())) {
                        ChannelGroupUtil.writeAndFlush(List.of(inboundFriendReqStatusUpdate.getRecvUserId()),
                                new RespEntity(MsgTypeEnum.FRIEND_STATUS_UPDATE, null));
                    }
                    channel.writeAndFlush(new RespEntity(MsgTypeEnum.FRIEND_STATUS_UPDATE, null));
                });
    }

    /**
     * 处理好友删除
     * @param channel
     * @param reqEntity
     */
    public void handleFriendDel(Channel channel, ReqEntity reqEntity) {
        InboundFriendDel inboundFriendDel = JsonUtil.parseObject(reqEntity.getContent(), InboundFriendDel.class);
        httpService.deleteFriend(reqEntity.getSendUserId(), inboundFriendDel.getFriendId(),
                friendDelVO -> {
                    ChannelGroupUtil.writeAndFlush(List.of(friendDelVO.getRecvUserId()),
                            new RespEntity(MsgTypeEnum.FRIEND_DEL, null));
                    channel.writeAndFlush(new RespEntity(MsgTypeEnum.FRIEND_DEL, null));
                });
    }

    /**
     * 处理群聊
     * @param channel
     * @param reqEntity
     */
    public void handleGroupChat(Channel channel, ReqEntity reqEntity) {
        InboundGroupMsg inboundGroupMsg = JsonUtil.parseObject(reqEntity.getContent(), InboundGroupMsg.class);
        httpService.getUserIdsByGroupId(inboundGroupMsg.getGroupId(), users -> {
            List<Long> userIds = users.parallelStream().map(Long::valueOf).collect(Collectors.toList());

            OutboundGroupMsg outboundGroupMsg = new OutboundGroupMsg();
            BeanUtil.copyProperties(inboundGroupMsg, outboundGroupMsg);
            outboundGroupMsg.setSendUserId(reqEntity.getSendUserId());
            outboundGroupMsg.setCreateTime(new Date());

            ChannelGroupUtil.writeAndFlush(userIds,
                    new RespEntity(MsgTypeEnum.GROUP_CHAT, JsonUtil.toJSONString(outboundGroupMsg)));
        });
    }

    /**
     * 处理入群请求
     * @param channel
     * @param reqEntity
     */
    public void handleReqJoinGroup(Channel channel, ReqEntity reqEntity) {
        // TODO
    }

    /**
     * 入群请求状态修改
     * @param channel
     */
    public void handleReqJoinGroupStatusUpdate(Channel channel, ReqEntity reqEntity) {
        // TODO
    }

    /**
     * 处理群加入邀请
     * @param channel
     * @param reqEntity
     */
    public void handleInviteJoinGroup(Channel channel, ReqEntity reqEntity) {
        // TODO
    }

    /**
     * 入群邀请状态修改
     * @param channel
     * @param reqEntity
     */
    public void handleInviteJoinGroupStatusUpdate(Channel channel, ReqEntity reqEntity) {
        // TODO
    }
}
