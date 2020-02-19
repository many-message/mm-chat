package cn.finull.mm.chat.service.http;

import cn.finull.mm.chat.entity.req.InboundFriendReq;
import cn.finull.mm.chat.entity.req.InboundPrivateMsg;
import cn.finull.mm.chat.service.http.param.FriendReqParam;
import cn.finull.mm.chat.service.http.param.MsgParam;
import cn.finull.mm.chat.service.http.vo.FriendDelVO;
import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    private final HttpTemplate httpTemplate = HttpTemplate.build();

    /**
     * 向数据库添加一条未签收消息
     * @param sendUserId
     * @param inboundPrivateMsg
     * @param callback 回调函数
     */
    public void addMsg(Long sendUserId, InboundPrivateMsg inboundPrivateMsg, Callback callback) {
        MsgParam msgParam = new MsgParam();
        msgParam.setSendUserId(sendUserId);
        msgParam.setRecvUserId(inboundPrivateMsg.getRecvUserId());
        msgParam.setMsgContent(inboundPrivateMsg.getMsg());
        msgParam.setMsgAddition(inboundPrivateMsg.getAddition());
        httpTemplate.post("/private/api/message", msgParam, callback);
    }

    /**
     * 添加一条好友请求
     * @param reqUserId
     * @param inboundFriendReq
     * @param callback
     */
    public void addFriendReq(Long reqUserId, InboundFriendReq inboundFriendReq, Callback callback) {
        FriendReqParam friendReqParam = new FriendReqParam();
        friendReqParam.setReqUserId(reqUserId);
        friendReqParam.setRecUserId(inboundFriendReq.getRecvUserId());
        friendReqParam.setReqMsg(inboundFriendReq.getMsg());
        httpTemplate.post("/private/api/friend-reqs", friendReqParam, callback);
    }

    /**
     * 修改好友请求状态
     * @param friendReqId
     * @param friendReqStatus
     * @param callback
     */
    public void updateFriendReqStatus(Long friendReqId, String friendReqStatus, Callback callback) {
        String url = String.format("/private/api/friend-reqs/%d?friendReqStatus=%s", friendReqId, friendReqStatus);
        httpTemplate.patch(url, null, callback);
    }

    /**
     * 删除好友
     * @param userId
     * @param friendId
     * @param consumer
     */
    public void deleteFriend(Long userId, Long friendId, Consumer<FriendDelVO> consumer) {
        String url = String.format("/private/api/%d/friends/%d", userId, friendId);
        httpTemplate.delete(url, new FriendDelVO(), consumer);
    }

    /**
     * 获取一个群的所有用户id集合
     * @param groupId
     * @param consumer
     */
    public void getUserIdsByGroupId(Long groupId, Consumer<List<String>> consumer) {
        String url = String.format("/private/api/%d/group-members", groupId);
        httpTemplate.get(url, CollUtil.newArrayList(), consumer);
    }
}
