package cn.finull.mm.chat.util;

import cn.finull.mm.chat.exception.JacksonProcessingException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Description
 * <p> json解析工具类
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-17 11:13
 */
@Slf4j
public final class JsonUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();

        // 设置时间格式转换
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 忽略无法转换的对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 如果存在未知属性，则忽略不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Long 序列化为String
        SimpleModule ltm = new SimpleModule();
        ltm.addSerializer(Long.class, ToStringSerializer.instance);
        ltm.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(ltm);
    }

    private JsonUtil() {}

    /**
     * 输出JSON串
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        try {
            return null == obj ? null : mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Object to JSON error!", e);
            throw new JacksonProcessingException(e);
        }
    }

    /**
     * 输出美化的JSON串
     *
     * @param obj
     * @param prettyFormat
     * @return
     */
    public static String toJSONString(Object obj, boolean prettyFormat) {
        try {
            if (prettyFormat) {
                return null == obj ? null : mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } else {
                return null == obj ? null : mapper.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.error("Object to JSON error!", e);
            throw new JacksonProcessingException(e);
        }
    }

    /**
     * 解析为JsonNode
     *
     * @param value
     * @return
     */
    public static JsonNode parseObject(String value) {
        try {
            return StrUtil.isBlank(value) ? null : mapper.readTree(value);
        } catch (JsonProcessingException e) {
            log.error("JSON to JsonNode error!", e);
            throw new JacksonProcessingException(e);
        }
    }

    /**
     * 解析为对象
     *
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String value, Class<T> tClass) {
        try {
            return StrUtil.isBlank(value) ? null : mapper.readValue(value, tClass);
        } catch (JsonProcessingException e) {
            log.error("JSON to Object error!", e);
            throw new JacksonProcessingException(e);
        }
    }

    /**
     * 解析为数组
     *
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String value, Class<T> tClass) {
        try {
            return StrUtil.isBlank(value) ? null : mapper.readValue(value, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("JSON to Array error!", e);
            throw new JacksonProcessingException(e);
        }
    }
}
