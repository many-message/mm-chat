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
 * @date 2020-05-06 23:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundDelGroup {
    private List<Long> recvUserIds;
}
