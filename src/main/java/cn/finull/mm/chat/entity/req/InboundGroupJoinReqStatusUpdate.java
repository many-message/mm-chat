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
 * @date 2020-02-22 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundGroupJoinReqStatusUpdate {
    private Long groupJoinReqId;
    /**
     * 1-请求中；2-同意；3-忽略
     */
    private String groupJoinReqStatus;
}
