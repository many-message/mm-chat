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
 * @date 2020-02-19 22:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendReqParam {
    private Long reqUserId;
    private Long recUserId;
    private String reqMsg;
}
