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
    FRIEND_REQ("301", "好友请求"),
    FRIEND_STATUS_UPDATE("302", "好友状态修改"),
    FRIEND_DEL("303", "删除好友"),
    GROUP_CHAT("400", "群聊"),
    REQ_JOIN_GROUP("401", "请求加入群"),
    REQ_JOIN_GROUP_STATUS_UPDATE("402", "请求入群状态修改"),
    INVITE_JOIN_GROUP("403", "邀请加入群"),
    INVITE_JOIN_GROUP_STATUS_UPDATE("404", "邀请入群状态修改");

    @JsonValue
    private String code;
    private String desc;
}
