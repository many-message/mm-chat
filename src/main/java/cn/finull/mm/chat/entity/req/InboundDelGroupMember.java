package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-05-06 23:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundDelGroupMember {
    private Long recvUserId;
}
