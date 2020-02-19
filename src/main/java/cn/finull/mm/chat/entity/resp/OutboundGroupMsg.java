package cn.finull.mm.chat.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description
 * <p> 群聊消息
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-20 0:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundGroupMsg {
    private Long groupId;
    private Long sendUserId;
    private Long sendGroupMemberId;
    private String msg;
    private String addition;
    private Date createTime;
}
