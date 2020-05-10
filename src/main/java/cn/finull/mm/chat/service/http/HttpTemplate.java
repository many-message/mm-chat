package cn.finull.mm.chat.service.http;

import cn.finull.mm.chat.util.JsonUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
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

    private static final String OK = "200";
    private static final String CODE= "code";
    private static final String MESSAGE = "message";
    private static final String DATA = "data";

    public static HttpTemplate build(String rootUri) {
        return Build.HTTP_SERVICE.setRootUri(rootUri);
    }

    private static class Build {
        private static final HttpTemplate HTTP_SERVICE = new HttpTemplate();
    }

    private String rootUri;

    public HttpTemplate setRootUri(String rootUri) {
        this.rootUri = rootUri;
        return this;
    }

    /**
     * 异步调用get请求
     * @param url api地址
     * @param respClz
     * @param consumer 消费响应数据
     * @param <T> 响应实体类型
     */
    public <T> void getForObject(String url, Class<T> respClz, Consumer<T> consumer) {
        HttpRequest request = HttpRequest.newBuilder(createUri(url)).build();
        sendAsyncForObject(request, respClz, consumer);
    }

    /**
     * 异步调用get请求
     * @param url api地址
     * @param respClz
     * @param consumer 消费响应数据
     * @param <T> 响应实体类型
     */
    public <T> void getForArray(String url, Class<T> respClz, Consumer<List<T>> consumer) {
        HttpRequest request = HttpRequest.newBuilder(createUri(url)).build();
        sendAsyncForArray(request, respClz, consumer);
    }

    /**
     * post请求
     * @param url
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void postForObject(String url, Object req, Class<T> respClz, Consumer<T> consumer) {
        sendAsyncForObject(buildPostRequest(url, req), respClz, consumer);
    }

    /**
     * post请求
     * @param url
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void postForArray(String url, Object req, Class<T> respClz, Consumer<List<T>> consumer) {
        sendAsyncForArray(buildPostRequest(url, req), respClz, consumer);
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
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void putForObject(String url, Object req, Class<T> respClz, Consumer<T> consumer) {
        sendAsyncForObject(buildPutRequest(url, req), respClz, consumer);
    }

    /**
     * put请求
     * @param url
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void putForArray(String url, Object req, Class<T> respClz, Consumer<List<T>> consumer) {
        sendAsyncForArray(buildPutRequest(url, req), respClz, consumer);
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
     * patch请求
     * @param url
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void patchForObject(String url, Object req, Class<T> respClz, Consumer<T> consumer) {
        sendAsyncForObject(buildPatchRequest(url, req), respClz, consumer);
    }

    /**
     * patch请求
     * @param url
     * @param req
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void patchForArray(String url, Object req, Class<T> respClz, Consumer<List<T>> consumer) {
        sendAsyncForArray(buildPatchRequest(url, req), respClz, consumer);
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
     * delete请求
     * @param url
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void deleteForObject(String url, Class<T> respClz, Consumer<T> consumer) {
        sendAsyncForObject(buildDeleteRequest(url), respClz, consumer);
    }

    /**
     * delete请求
     * @param url
     * @param respClz
     * @param consumer
     * @param <T>
     */
    public <T> void deleteForArray(String url, Class<T> respClz, Consumer<List<T>> consumer) {
        sendAsyncForArray(buildDeleteRequest(url), respClz, consumer);
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
        return URI.create(rootUri + url);
    }

    private <T> void sendAsyncForObject(HttpRequest request, Class<T> clz, Consumer<T> consumer) {
        sendAsync(request)
                .thenAccept(content -> {
                    JsonNode jsonNode = JsonUtil.parseObject(content);
                    if (OK.equals(jsonNode.get(CODE).toString())) {
                        String data = jsonNode.get(DATA).toString();
                        consumer.accept(JsonUtil.parseObject(data, clz));
                        return;
                    }
                    log.error("Http 远程调用错误 -- {}！", jsonNode.get(MESSAGE).toString());
                }).join();
    }

    private <T> void sendAsyncForArray(HttpRequest request, Class<T> clz, Consumer<List<T>> consumer) {
        sendAsync(request)
                .thenAccept(content -> {
                    JsonNode jsonNode = JsonUtil.parseObject(content);
                    if (OK.equals(jsonNode.get(CODE).asText())) {
                        String data = jsonNode.get(DATA).toString();
                        consumer.accept(JsonUtil.parseArray(data, clz));
                        return;
                    }
                    log.error("Http 远程调用错误 -- {}！", jsonNode.get(MESSAGE).toString());
                }).join();
    }

    private <T> void sendAsync(HttpRequest request, Callback callback) {
        sendAsync(request)
                .thenAccept(content -> {
                    JsonNode jsonNode = JsonUtil.parseObject(content);
                    if (OK.equals(jsonNode.get(CODE).asText())) {
                        callback.accept();
                        return;
                    }
                    log.error("Http 远程调用错误 -- {}！", jsonNode.get(MESSAGE).toString());
                }).join();
    }

    private CompletableFuture<String> sendAsync(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
