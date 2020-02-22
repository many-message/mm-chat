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
 * @date 2020-02-22 11:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupJoinReqParam {
    private Long reqUserId;
    private Long groupId;
    private String reqMsg;
}
