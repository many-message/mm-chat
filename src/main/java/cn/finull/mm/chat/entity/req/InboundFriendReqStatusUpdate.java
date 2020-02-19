package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p> 修改好友请求状态
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 22:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundFriendReqStatusUpdate {
    /**
     * 接收者用户id
     */
    private Long recvUserId;
    /**
     * 好友请求id
     */
    private Long friendReqId;
    /**
     * 好友状态：1-请求中；2-已同意；3-已忽略
     */
    private String friendReqStatus;
}
