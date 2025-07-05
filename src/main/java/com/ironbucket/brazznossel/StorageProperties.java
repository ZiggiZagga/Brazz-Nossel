package com.ironbucket.brazznossel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "ironbucket")
class StorageProperties {
    private Map<String, StorageConfig> storages = new HashMap<>();
    public Map<String, StorageConfig> getStorages() {
        return storages;
    }
    public void setStorages(Map<String, StorageConfig> storages) {
        this.storages = storages;
    }
}
