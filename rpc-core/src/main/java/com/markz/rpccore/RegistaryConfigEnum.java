package com.markz.rpccore;

public enum RegistaryConfigEnum {

    ZK_PORT("rpc.registry.port"),
    ZK_HOST("rpc.registry.host"),
    ZK_ROOT("rpc.registry.zkRoot"),
    ZK_TIMEOUT("rpc.registry.timeout");

    private final String key;

    RegistaryConfigEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
