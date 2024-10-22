package com.markz.rpccore.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 读取 yml 配置文件
 */
@Slf4j
public class ConfigLoader {

    /**
     * 配置缓存
     */
    private static Map<String, Object> config;

    public ConfigLoader(String filePath) {
        loadConfig(filePath);
    }

    private void loadConfig(String filePath) {
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(filePath));
            config = yaml.load(inputStream);
        } catch (IOException e) {
            log.info("读取配置出错：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Object getValue(String key) {
        return config.get(key);
    }
}
