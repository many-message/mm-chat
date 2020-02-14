package cn.finull.mm.chat.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Description
 * <p>
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-13 20:00
 */
@Slf4j
public class ChatServer {

    /**
     * boss线程组数量
     */
    private static final int BOSS_GROUP_NUM = 1;
    /**
     * 服务器可连接队列大小
     */
    private static final int CHANNEL_SO_BACKLOG = 128;
    /**
     * 保持活动连接状态
     */
    private static final boolean CHANNEL_SO_KEEPALIVE = true;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;

    public ChatServer() {
        bossGroup = new NioEventLoopGroup(BOSS_GROUP_NUM);
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
    }

    public void bootStrap(ChannelInitializer<SocketChannel> channelInitializer) {
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, CHANNEL_SO_BACKLOG)
                .childOption(ChannelOption.SO_KEEPALIVE, CHANNEL_SO_KEEPALIVE)
                .childHandler(channelInitializer);
    }

    public void start(int port) {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port);

            log.info("服务器已启动，请访问[{}]端口！", port);

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务器宕机！", e);
        } finally {
            close();
        }
    }

    private void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        log.info("资源清理完毕，程序关闭！");
    }
}
