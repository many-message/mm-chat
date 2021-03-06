package cn.finull.mm.chat.entity.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-22 11:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundGroupJoinReq {
    private List<Long> recvUserIds;
    private String nickname;
    private String groupName;
}
