package cn.finull.mm.chat.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-05-10 11:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundGroupJoinReq {
    private String nickname;
    private String groupName;
}
