package cn.finull.mm.chat;

/**
 * Description
 * <p> 应用主类
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-13 18:01
 */
public class MmChatApplication {

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.bootStrap(new HandlerInitializer());
        server.start(8888);
    }
}
