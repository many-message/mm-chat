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
 * @date 2020-02-19 21:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgParam {
    private Long sendUserId;
    private Long recvUserId;
    private String msgContent;
    private String msgAddition;
}
