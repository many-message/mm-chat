package cn.finull.mm.chat.service.http.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 23:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDelVO {
    private Long sendUserId;
    private Long recvUserId;
}
