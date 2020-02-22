package cn.finull.mm.chat.service.http.vo;

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
 * @date 2020-02-22 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupJoinReqVO {
    private List<Long> recUserIds;
}
