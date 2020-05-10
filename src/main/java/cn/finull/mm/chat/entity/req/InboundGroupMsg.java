package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description
 * <p> 群聊消息
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-20 0:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundGroupMsg {
    /**
     * 群id
     */
    private Long groupId;
    /**
     * 发送消息的成员id
     */
    private Long sendGroupMemberId;
    /**
     * 群备注
     */
    private String groupMemberName;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 接收者
     */
    private List<Long> recvUserIds;
    /**
     * 消息
     * len:255
     */
    private String msgContent;
    /**
     * 附加消息
     * len:255
     */
    private String msgAddition;
}
