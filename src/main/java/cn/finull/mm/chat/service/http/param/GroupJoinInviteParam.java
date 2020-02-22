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
 * @date 2020-02-22 11:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupJoinInviteParam {
    private Long groupId;
    private Long reqUserId;
    private Long inviteUserId;
}
