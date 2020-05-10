package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p> 好友删除请求
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 23:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundDelFriend {
    /**
     * 接收者ID
     */
    private Long recvUserId;

    private String nickname;
}
