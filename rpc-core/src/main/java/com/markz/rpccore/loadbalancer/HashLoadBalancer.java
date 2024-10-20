package com.markz.rpccore.loadbalancer;

import cn.hutool.core.net.NetUtil;
import com.markz.rpccore.model.ServiceProviderMeta;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * IP Hash 负载均衡器
 */
public class HashLoadBalancer implements LoadBalance {

    @Override
    public ServiceProviderMeta selectServer(List<ServiceProviderMeta> serviceMetaList) {
        LinkedHashSet<String> ipv4s = NetUtil.localIpv4s();
        Object[] ipv4Array = ipv4s.stream().toArray();
        String ipv4 = (String) ipv4Array[0];

        int ipHash = ipv4.hashCode();
        // hashcode 可能是负数，所以取个绝对值
        int index = Math.abs(ipHash % serviceMetaList.size());
        return serviceMetaList.get(index);
    }
}
