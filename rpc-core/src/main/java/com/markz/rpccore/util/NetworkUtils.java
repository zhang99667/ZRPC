package com.markz.rpccore.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络工具类
 */
public class NetworkUtils {
    /**
     * 获取本机 IPV4 地址
     *
     * @return ipv4 地址
     */
    public static String getLocalHostIp() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            if (localAddress instanceof Inet4Address) {
                return localAddress.getHostAddress(); // 获取本机的 IPv4 地址
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("无法获取本地 IP 地址", e);
        }
        return null;
    }
}
