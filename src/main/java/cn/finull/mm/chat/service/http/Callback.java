package cn.finull.mm.chat.service.http;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 22:01
 */
@FunctionalInterface
public interface Callback {
    /**
     * 回调函数
     */
    void accept();
}
