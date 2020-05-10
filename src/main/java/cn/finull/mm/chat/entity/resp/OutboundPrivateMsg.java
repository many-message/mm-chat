package cn.finull.mm.chat.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description
 * <p> 出站私聊消息
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 21:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundPrivateMsg {
    /**
     * 消息发送者
     */
    private Long sendUserId;
    /**
     * 消息正文
     * len:255
     */
    private String msgContent;
    /**
     * 消息附加值
     * len:255
     */
    private String msgAddition;
    /**
     * 发送时间
     */
    private Date createTime;
}
