package cn.finull.mm.chat.service.http;

import cn.finull.mm.chat.config.HttpConfig;
import cn.finull.mm.chat.entity.req.*;
import cn.finull.mm.chat.service.http.param.MsgAddParam;

/**
 * Description
 * <p> 调用外部应用接口
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 21:47
 */
public class HttpService {

    private HttpService() {}

    public static HttpService build() {
        return Build.HTTP_SERVICE;
    }

    private static class Build {
        private static final HttpService HTTP_SERVICE = new HttpService();
    }

    private final HttpTemplate httpTemplate = HttpTemplate.build(HttpConfig.ROOT_URI);

    /**
     * 向数据库添加一条未签收消息
     * @param sendUserId
     * @param inboundPrivateMsg
     * @param callback 回调函数
     */
    public void addMsg(Long sendUserId, InboundPrivateMsg inboundPrivateMsg, Callback callback) {
        MsgAddParam msgAddParam = new MsgAddParam();
        msgAddParam.setSendUserId(sendUserId);
        msgAddParam.setRecvUserId(inboundPrivateMsg.getRecvUserId());
        msgAddParam.setMsgContent(inboundPrivateMsg.getMsg());
        msgAddParam.setMsgAddition(inboundPrivateMsg.getAddition());
        httpTemplate.post("/private/api/message", msgAddParam, callback);
    }
}
