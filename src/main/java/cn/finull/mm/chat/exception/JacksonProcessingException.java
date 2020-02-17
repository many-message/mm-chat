package cn.finull.mm.chat.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Description
 * <p> json解析错误时抛出的异常
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 11:19
 */
public class JacksonProcessingException extends RuntimeException {
    public JacksonProcessingException(JsonProcessingException e) {
        super(e);
    }
}
