package cn.finull.mm.chat.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 10:52
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum {
    /**
     *
     */
    CONN("100", "连接"),
    IDLE("200", "心跳检测"),
    PRIVATE_CHAT("300", "私聊"),
    FRIEND_REQ_NOTICE("301", "好友请求通知"),
    DEL_FRIEND_NOTICE("302", "删除好友通知"),
    GROUP_CHAT("400", "群聊"),
    REQ_JOIN_GROUP_NOTICE("401", "请求加入群通知"),
    INVITE_JOIN_GROUP_NOTICE("402", "邀请加入群通知"),
    DEL_GROUP_MEMBER_NOTICE("403", "群成员删除通知"),
    DEL_GROUP_NOTICE("404", "群解散通知");

    @JsonValue
    private String code;
    private String desc;
}
