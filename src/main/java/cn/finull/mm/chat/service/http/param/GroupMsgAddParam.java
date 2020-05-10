package cn.finull.mm.chat.service.http.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-05-10 18:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgAddParam {
    private Long groupId;
    private Long sendGroupMemberId;
    private Long sendUserId;
    private String msgContent;
    private String msgAddition;
}
