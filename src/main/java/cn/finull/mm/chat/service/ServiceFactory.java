package cn.finull.mm.chat.service;

import cn.finull.mm.chat.entity.enums.MsgTypeEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 13:57
 */
public final class ServiceFactory {

    private ServiceFactory() { }

    private static final ConcurrentMap<MsgTypeEnum, MessageHandler> SERVICE_HANDLERS = new ConcurrentHashMap<>();

    static {
        ChatService chatService = new ChatService();
        SERVICE_HANDLERS.put(MsgTypeEnum.CONN, chatService::handleConn);
        SERVICE_HANDLERS.put(MsgTypeEnum.IDLE, chatService::handleIdle);
        SERVICE_HANDLERS.put(MsgTypeEnum.PRIVATE_CHAT, chatService::handlePrivateChat);
        SERVICE_HANDLERS.put(MsgTypeEnum.FRIEND_REQ_NOTICE, chatService::handleFriendReq);
        SERVICE_HANDLERS.put(MsgTypeEnum.DEL_FRIEND_NOTICE, chatService::handleDelFriend);
        SERVICE_HANDLERS.put(MsgTypeEnum.GROUP_CHAT, chatService::handleGroupChat);
        SERVICE_HANDLERS.put(MsgTypeEnum.REQ_JOIN_GROUP_NOTICE, chatService::handleReqJoinGroup);
        SERVICE_HANDLERS.put(MsgTypeEnum.INVITE_JOIN_GROUP_NOTICE, chatService::handleInviteJoinGroup);
        SERVICE_HANDLERS.put(MsgTypeEnum.DEL_GROUP_MEMBER_NOTICE, chatService::handleDelGroupMember);
        SERVICE_HANDLERS.put(MsgTypeEnum.DEL_GROUP_NOTICE, chatService::handleDelGroup);
    }

    public static MessageHandler buildChatService(MsgTypeEnum msgType) {
        return SERVICE_HANDLERS.get(msgType);
    }
}
