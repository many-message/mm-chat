package cn.finull.mm.chat.util;

import cn.finull.mm.chat.entity.RespEntity;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 14:23
 */
public final class ChannelGroupUtil {

    private ChannelGroupUtil() {}

    private static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final ConcurrentMap<Channel, Long> CHANNEL_IDS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Long, Channel> ID_CHANNELS = new ConcurrentHashMap<>();

    /**
     * 将Channel添加到ChannelGroup中
     * @param channel
     */
    public static void addChannel(Channel channel) {
        CHANNELS.add(channel);
    }

    /**
     * 关联Channel和userId
     * @param channel
     * @param userId
     */
    public static void put(Channel channel, Long userId) {
        CHANNEL_IDS.put(channel, userId);
        ID_CHANNELS.put(userId, channel);
    }

    /**
     * 移除Channel
     * @param channel
     * @return userId
     */
    public static Long remove(Channel channel) {
        Long userId = CHANNEL_IDS.remove(channel);
        if (userId != null) {
            ID_CHANNELS.remove(userId);
        }
        return userId;
    }

    /**
     * 向ChannelGroup中写入数据
     * @param recvUserIds
     * @param respEntity
     */
    public static void writeAndFlush(List<Long> recvUserIds, RespEntity respEntity) {
        Set<Channel> recvChannels = recvUserIds.parallelStream()
                .map(ID_CHANNELS::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        CHANNELS.writeAndFlush(respEntity, recvChannels::contains);
    }

    /**
     * 判断当前用户是否在线
     * @param userId
     * @return
     */
    public static boolean isActive(Long userId) {
        return ID_CHANNELS.containsKey(userId);
    }

    /**
     * 获取当前在线连接数
     * @return
     */
    public static int channelSize() {
        return CHANNELS.size();
    }
}
