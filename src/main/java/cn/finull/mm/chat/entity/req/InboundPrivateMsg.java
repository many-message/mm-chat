package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description
 * <p> 入站私聊消息
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundPrivateMsg {
    /**
     * 消息接收者
     */
    private Long recvUserId;
    /**
     * 消息正文
     * len:255
     */
    private String msgContent;
    /**
     * 附加值
     * len:255
     */
    private String msgAddition;
}
