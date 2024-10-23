package com.markz.rpccore.holder;

import com.markz.rpccore.request.RequestPromise;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * RpcRequestHolder
 */
public class RpcRequestHolder {

    /**
     * RpcRequest 和 Promise 的映射
     * <p>
     * 通过 ConcurrentHashMap 绑定 requestId 和 RequestPromise
     * 保证并发请求下响应的幂等性
     */
    public static final ConcurrentHashMap<String, RequestPromise> REQUEST_PROMISE_MAP;

    static {
        REQUEST_PROMISE_MAP = new ConcurrentHashMap<>();
    }

    /**
     * 添加 requestId 和 requestPromise 映射
     *
     * @param requestId      requestId
     * @param requestPromise RequestPromise
     */
    public static void addRequestPromise(String requestId, RequestPromise requestPromise) {
        REQUEST_PROMISE_MAP.put(requestId, requestPromise);
    }

    /**
     * 获取 requestPromise
     *
     * @param requestId requestId
     * @return requestPromise
     */
    public static RequestPromise getRequestPromise(String requestId) {
        return REQUEST_PROMISE_MAP.get(requestId);
    }

    /**
     * 用完及时移除 Promise 防止 OOM
     *
     * @param requestId requestId
     */
    public static void removeRequestPromise(String requestId) {
        REQUEST_PROMISE_MAP.remove(requestId);
    }

    /**
     * IP:port 和 Channel 的映射
     * <p>
     * 通过绑定为了 IP:port 和 Channel
     * 复用 Channel，避免每次都创建 Channel 导致的性能损失
     */
    private static final ConcurrentHashMap<String, Channel> CHANNEL_MAPPING_MAP;

    static {
        CHANNEL_MAPPING_MAP = new ConcurrentHashMap<>();
    }

    /**
     * Channel 是否存在
     *
     * @param serverIp   serverIp
     * @param serverPort serverPort
     * @return Boolean
     */
    public static Boolean channelExist(String serverIp, int serverPort) {
        return getChannel(serverIp, serverPort) != null;
    }

    /**
     * 添加 Channel
     *
     * @param serverIp   serverIp
     * @param serverPort serverPort
     * @param channel    Channel
     */
    public static void addChannelMapping(String serverIp, int serverPort, Channel channel) {
        String key = getChannelMappingKeyByServerIpAndPort(serverIp, serverPort);
        CHANNEL_MAPPING_MAP.put(key, channel);
    }

    /**
     * 根据 serverIp:serverPort 获取 ChannelMapping
     *
     * @param serverIp   serverIp
     * @param serverPort serverPort
     * @return ChannelMapping
     */
    public static Channel getChannel(String serverIp, int serverPort) {
        String key = getChannelMappingKeyByServerIpAndPort(serverIp, serverPort);
        return CHANNEL_MAPPING_MAP.get(key);
    }

    /**
     * 用完及时移除 Channel 防止 OOM
     *
     * @param serverIp   serverIp
     * @param serverPort serverPort
     */
    public static void removeChannelMapping(String serverIp, int serverPort) {
        String key = getChannelMappingKeyByServerIpAndPort(serverIp, serverPort);
        CHANNEL_MAPPING_MAP.remove(key);
    }

    /**
     * 拼接 serverIp:serverPort
     *
     * @param serverIp   serverIp
     * @param serverPort serverPort
     * @return serverIp:serverPort
     */
    private static String getChannelMappingKeyByServerIpAndPort(String serverIp, int serverPort) {
        return serverIp + ":" + serverPort;
    }
}
