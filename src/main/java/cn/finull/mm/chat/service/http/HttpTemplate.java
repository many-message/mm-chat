package cn.finull.mm.chat.service.http;

import cn.finull.mm.chat.config.HttpConfig;
import cn.finull.mm.chat.util.JsonUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Description
 * <p> http请求业务
 * Copyright (C) HPE, All rights reserved.
 *
 * @author Ma, Chenxi
 * @date 2020-02-19 19:59
 */
@Slf4j
public class HttpTemplate {

    private final HttpClient httpClient;
    private HttpTemplate() {
        httpClient = HttpClient.newHttpClient();
    }

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    public static HttpTemplate build() {
        return Build.HTTP_SERVICE;
    }

    private static class Build {
        private static final HttpTemplate HTTP_SERVICE = new HttpTemplate();
    }

    /**
     * 异步调用get请求
     * @param url api地址
     * @param t 响应实体对象，请求成功后，响应数据将会被注入到这个对象中
     * @param consumer 消费响应数据
     * @param <T> 响应实体类型
     */
    public <T> void get(String url, T t, Consumer<T> consumer) {
        HttpRequest request = HttpRequest.newBuilder(createUri(url)).build();
        sendAsync(request, t, consumer);
    }

    /**
     * post请求
     * @param url
     * @param req 请求体
     * @param t
     * @param consumer
     * @param <T>
     */
    public <T> void post(String url, Object req, T t, Consumer<T> consumer) {
        sendAsync(buildPostRequest(url, req), t, consumer);
    }

    /**
     * ignore
     * @param url
     * @param req
     * @param callback
     */
    public void post(String url, Object req, Callback callback) {
        sendAsync(buildPostRequest(url, req), callback);
    }

    private HttpRequest buildPostRequest(String url, Object req) {
        return HttpRequest.newBuilder(createUri(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(
                        StrUtil.blankToDefault(JsonUtil.toJSONString(req), "")))
                .build();
    }

    /**
     * put请求
     * @param url
     * @param req 请求体
     * @param t
     * @param consumer
     * @param <T>
     */
    public <T> void put(String url, Object req, T t, Consumer<T> consumer) {
        sendAsync(buildPutRequest(url, req), t, consumer);
    }

    /**
     * ignore
     * @param url
     * @param req
     * @param callback
     */
    public void put(String url, Object req, Callback callback) {
        sendAsync(buildPutRequest(url, req), callback);
    }

    private HttpRequest buildPutRequest(String url, Object req) {
        return HttpRequest.newBuilder(createUri(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(
                        StrUtil.blankToDefault(JsonUtil.toJSONString(req), "")))
                .build();
    }

    /**
     * ignore
     * @param url
     * @param req
     * @param t
     * @param consumer
     * @param <T>
     */
    public <T> void patch(String url, Object req, T t, Consumer<T> consumer) {
        sendAsync(buildPatchRequest(url, req), t, consumer);
    }

    /**
     * patch请求
     * @param url
     * @param req
     * @param callback
     */
    public void patch(String url, Object req, Callback callback) {
        sendAsync(buildPatchRequest(url, req), callback);
    }

    private HttpRequest buildPatchRequest(String url, Object req) {
        return HttpRequest.newBuilder(createUri(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers
                        .ofString(StrUtil.blankToDefault(JsonUtil.toJSONString(req), "")))
                .build();
    }

    /**
     * ignore
     * @param url
     * @param t
     * @param consumer
     * @param <T>
     */
    public <T> void delete(String url, T t, Consumer<T> consumer) {
        sendAsync(buildDeleteRequest(url), t, consumer);
    }

    /**
     * delete请求
     * @param url
     * @param callback
     * @param <T>
     */
    public <T> void delete(String url, Callback callback) {
        sendAsync(buildDeleteRequest(url), callback);
    }

    private HttpRequest buildDeleteRequest(String url) {
        return HttpRequest.newBuilder(createUri(url)).DELETE().build();
    }

    private URI createUri(String url) {
        return URI.create(HttpConfig.ROOT_URI + url);
    }

    private <T> void sendAsync(HttpRequest request, T t, Consumer<T> consumer) {
        sendAsync(request)
                .thenAccept(resp -> {
                    if (resp.isSuccess()) {
                        if (t != null) {
                            BeanUtil.copyProperties(resp.getData(), t);
                        }
                        consumer.accept(t);
                    }
                    log.error("Http 远程调用错误 -- {}！", resp.getMessage());
                })
                .join();
    }

    private <T> void sendAsync(HttpRequest request, Callback callback) {
        sendAsync(request)
                .thenAccept(resp -> {
                    if (resp.isSuccess()) {
                        callback.accept();
                    }
                    log.error("Http 远程调用错误 -- {}！", resp.getMessage());
                })
                .join();
    }

    private CompletableFuture<Resp> sendAsync(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> JsonUtil.parseObject(body, Resp.class));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Resp<T> {
        private String code;
        private String message;
        private T data;
        public boolean isSuccess() {
            return "00".equals(code);
        }
    }
}
