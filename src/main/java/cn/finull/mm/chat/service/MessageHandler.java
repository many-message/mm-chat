package cn.finull.mm.chat.service;

import cn.finull.mm.chat.entity.ReqEntity;
import io.netty.channel.Channel;

/**
 * Description
 * <p> 处理业务逻辑
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 13:59
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * 处理函数
     * @param channel
     * @param reqEntity
     * @return
     */
    void handle(Channel channel, ReqEntity reqEntity);
}
