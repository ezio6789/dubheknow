package com.insmess.knowledge.file.storage.service;


import com.insmess.knowledge.file.storage.properties.StorageProperties;
import com.obs.services.ObsClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 华为云存储
 *
 */
public class HuaweiStorageService extends StorageService {

    public HuaweiStorageService(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        ObsClient client = new ObsClient(properties.getHuawei().getAccessKey(),
                properties.getHuawei().getSecretKey(), properties.getHuawei().getEndPoint());
        try {
            client.putObject(properties.getHuawei().getBucketName(), path, inputStream);
            client.close();
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败：" + e.getMessage());
        }

        return properties.getConfig().getDomain() + "/" + path;
    }

}
