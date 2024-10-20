package com.markz.rpccore.model;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * 服务提供者的 Channel 映射关系，Channel 和 ip:port 绑定
 * 目的为了复用 Channel，所以需要和服务主机绑定
 */
@Data
public class ChannelMapping {

    /**
     * 服务主机节点，格式 ip:port
     */
    private String ipWithPort;

    /**
     * 服务主机 IP
     */
    private String ip;

    /**
     * 服务主机端口
     */
    private Integer port;

    /**
     * 客户端连接通道
     */
    private Channel channel;

    public ChannelMapping(String ip, Integer port, Channel channel) {
        this.ip = ip;
        this.port = port;
        this.channel = channel;
    }

    public String getIpWithPort() {
        return ip + ":" + port;
    }
}
