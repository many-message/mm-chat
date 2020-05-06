package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p> 好友请求消息
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundFriendReq {
    /**
     * 接收者用户id
     */
    private Long recvUserId;
}
