package com.volcengine.ecs.model;

import java.util.List;

/**
 * ECS实例创建请求参数模型
 */
public class ECSInstanceRequest {

    private String instanceName;
    private String description;
    private String instanceType;
    private String imageId;
    private String vpcId;
    private String subnetId;
    private String keyPairName;
    private String systemDiskType = "ESSD_PL0";
    private Integer systemDiskSize = 40;
    private Integer instanceCount = 1;
    private List<String> securityGroupIds;
    private String instanceChargeType = "POSTPAID_BY_HOUR";
    private Boolean publicIpEnabled = true;
    private Integer bandwidth = 1;
    private String zoneId;

    // Getters and Setters
    public String getInstanceName() {
        return instanceName;
    }

    public ECSInstanceRequest setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ECSInstanceRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public ECSInstanceRequest setInstanceType(String instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public String getImageId() {
        return imageId;
    }

    public ECSInstanceRequest setImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getVpcId() {
        return vpcId;
    }

    public ECSInstanceRequest setVpcId(String vpcId) {
        this.vpcId = vpcId;
        return this;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public ECSInstanceRequest setSubnetId(String subnetId) {
        this.subnetId = subnetId;
        return this;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public ECSInstanceRequest setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
        return this;
    }

    public String getSystemDiskType() {
        return systemDiskType;
    }

    public ECSInstanceRequest setSystemDiskType(String systemDiskType) {
        this.systemDiskType = systemDiskType;
        return this;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public ECSInstanceRequest setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
        return this;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public ECSInstanceRequest setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
        return this;
    }

    public List<String> getSecurityGroupIds() {
        return securityGroupIds;
    }

    public ECSInstanceRequest setSecurityGroupIds(List<String> securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
        return this;
    }

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public ECSInstanceRequest setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType;
        return this;
    }

    public Boolean getPublicIpEnabled() {
        return publicIpEnabled;
    }

    public ECSInstanceRequest setPublicIpEnabled(Boolean publicIpEnabled) {
        this.publicIpEnabled = publicIpEnabled;
        return this;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public ECSInstanceRequest setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public ECSInstanceRequest setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }
}