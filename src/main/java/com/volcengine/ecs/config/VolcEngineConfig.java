package com.volcengine.ecs.config;

import com.volcengine.ApiClient;
import com.volcengine.sign.Credentials;

/**
 * 火山引擎SDK配置类
 * 用于配置API密钥、区域等信息
 */
public class VolcEngineConfig {

    /**
     * 火山引擎API访问密钥ID (必填)
     * 获取地址: https://console.volcengine.com/iam/keymanage/
     */
    private String accessKeyId = "YOUR_ACCESS_KEY_ID";

    /**
     * 火山引擎API访问密钥Secret (必填)
     * 获取地址: https://console.volcengine.com/iam/keymanage/
     */
    private String accessKeySecret = "YOUR_ACCESS_KEY_SECRET";

    /**
     * 区域ID (必填)
     * 例如: cn-beijing, cn-shanghai, cn-guangzhou等
     */
    private String regionId = "cn-beijing";

    /**
     * 请求超时时间(秒)
     */
    private int connectionTimeout = 30;

    /**
     * 读取超时时间(秒)
     */
    private int socketTimeout = 60;

    // Getters and Setters
    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 创建ApiClient对象
     */
    public ApiClient createApiClient() {
        return new ApiClient()
                .setCredentials(Credentials.getCredentials(accessKeyId, accessKeySecret))
                .setRegion(regionId);
    }
}