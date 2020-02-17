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
    FRIEND_DEL("302", "删除好友"),
    GROUP_CHAT("400", "群聊"),
    REQ_JOIN_GROUP("401", "请求加入群"),
    INVITE_JOIN_GROUP("402", "邀请加入群");

    @JsonValue
    private String code;
    private String desc;
}
