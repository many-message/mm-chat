package cn.finull.mm.chat.entity;

import cn.finull.mm.chat.entity.enums.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p> 服务器接收实体
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-14 21:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEntity {
    /**
     * 发送者id
     */
    private Long sendUserId;
    /**
     * 消息类型
     */
    private MsgTypeEnum msgType;
    /**
     * 消息正文
     */
    private String content;
}
