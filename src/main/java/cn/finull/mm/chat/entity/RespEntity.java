package cn.finull.mm.chat.entity;

import cn.finull.mm.chat.entity.enums.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEntity {
    /**
     * 响应消息类型
     */
    private MsgTypeEnum msgType;
    /**
     * 消息正文
     */
    private String content;
}
