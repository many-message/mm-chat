package cn.finull.mm.chat.service.http;

import cn.finull.mm.chat.entity.req.*;
import cn.finull.mm.chat.service.http.param.FriendReqParam;
import cn.finull.mm.chat.service.http.param.GroupJoinInviteParam;
import cn.finull.mm.chat.service.http.param.GroupJoinReqParam;
import cn.finull.mm.chat.service.http.param.MsgParam;
import cn.finull.mm.chat.service.http.vo.FriendDelVO;
import cn.finull.mm.chat.service.http.vo.GroupJoinInviteVO;
import cn.finull.mm.chat.service.http.vo.GroupJoinReqVO;
import cn.hutool.core.bean.BeanUtil;
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
     * @param inboundFriendReqStatusUpdate
     * @param callback
     */
    public void updateFriendReqStatus(InboundFriendReqStatusUpdate inboundFriendReqStatusUpdate, Callback callback) {
        String url = String.format("/private/api/friend-reqs/%d?friendReqStatus=%s",
                inboundFriendReqStatusUpdate.getFriendReqId(),
                inboundFriendReqStatusUpdate.getFriendReqStatus());
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

    /**
     * 发送一个入群请求
     * @param reqUserId
     * @param inboundGroupJoinReq
     * @param consumer
     */
    public void addGroupJoinReq(Long reqUserId, InboundGroupJoinReq inboundGroupJoinReq, Consumer<GroupJoinReqVO> consumer) {
        GroupJoinReqParam groupJoinReqParam = new GroupJoinReqParam();
        groupJoinReqParam.setReqUserId(reqUserId);
        groupJoinReqParam.setGroupId(inboundGroupJoinReq.getGroupId());
        groupJoinReqParam.setReqMsg(inboundGroupJoinReq.getMsg());
        httpTemplate.post("/private/api/group-join-reqs", groupJoinReqParam, new GroupJoinReqVO(), consumer);
    }

    /**
     * 修改请求入群状态
     */
    public void updateGroupJoinReq(InboundGroupJoinReqStatusUpdate inboundGroupJoinReqStatusUpdate, Consumer<GroupJoinReqVO> consumer) {
        String url = String.format("/private/api/group-join-reqs/%d?groupJoinReqStatus=%s",
                inboundGroupJoinReqStatusUpdate.getGroupJoinReqId(),
                inboundGroupJoinReqStatusUpdate.getGroupJoinReqStatus());
        httpTemplate.patch(url, null, new GroupJoinReqVO(), consumer);
    }

    /**
     * 添加入群邀请
     * @param reqUserId
     * @param inboundGroupInviteReq
     * @param consumer
     */
    public void addGroupJoinInvite(Long reqUserId, InboundGroupInviteReq inboundGroupInviteReq, Consumer<GroupJoinInviteVO> consumer) {
        GroupJoinInviteParam groupJoinInviteParam = new GroupJoinInviteParam();
        BeanUtil.copyProperties(inboundGroupInviteReq, groupJoinInviteParam);
        groupJoinInviteParam.setReqUserId(reqUserId);
        httpTemplate.post("/private/api/group-join-invites", groupJoinInviteParam, new GroupJoinInviteVO(), consumer);
    }

    /**
     * 修改邀请入群状态
     * @param inboundGroupInviteReqStatusUpdate
     * @param consumer
     */
    public void updateGroupJoinInvite(InboundGroupInviteReqStatusUpdate inboundGroupInviteReqStatusUpdate, Consumer<GroupJoinInviteVO> consumer) {
        String url = String.format("/private/api/group-join-invites/%d?groupJoinInviteStatus=%s",
                inboundGroupInviteReqStatusUpdate.getGroupJoinInviteId(),
                inboundGroupInviteReqStatusUpdate.getGroupJoinInviteStatus());
        httpTemplate.patch(url, null, new GroupJoinInviteVO(), consumer);
    }
}
