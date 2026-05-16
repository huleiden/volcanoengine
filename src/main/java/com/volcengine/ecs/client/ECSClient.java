package com.volcengine.ecs.client;

import com.volcengine.ApiClient;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.config.VolcEngineConfig;
import com.volcengine.ecs.model.ECSInstanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 火山引擎ECS客户端封装类
 * 提供创建、查询、删除ECS实例等操作
 */
public class ECSClient {

    private static final Logger logger = LoggerFactory.getLogger(ECSClient.class);

    private final EcsApi ecsApi;

    /**
     * 使用配置创建ECS客户端
     */
    public ECSClient(VolcEngineConfig config) {
        ApiClient apiClient = config.createApiClient();
        this.ecsApi = new EcsApi(apiClient);

        logger.info("ECS客户端初始化完成, 区域: {}", config.getRegionId());
    }

    /**
     * 创建ECS实例
     *
     * @param request 创建请求参数
     * @return 创建结果，包含实例ID列表
     */
    public Object createInstance(ECSInstanceRequest request) {
        logger.info("开始创建ECS实例: {}", request.getInstanceName());

        try {
            // 构建系统盘配置
            com.volcengine.ecs.model.VolumeForRunInstancesInput systemDisk = 
                new com.volcengine.ecs.model.VolumeForRunInstancesInput()
                    .volumeType(request.getSystemDiskType())
                    .size(request.getSystemDiskSize());

            // 构建数据盘列表
            List<com.volcengine.ecs.model.VolumeForRunInstancesInput> volumes = new ArrayList<>();
            volumes.add(systemDisk);

            // 构建网络接口配置
            com.volcengine.ecs.model.NetworkInterfaceForRunInstancesInput networkInterface = 
                new com.volcengine.ecs.model.NetworkInterfaceForRunInstancesInput()
                    .subnetId(request.getSubnetId())
                    .securityGroupIds(request.getSecurityGroupIds());

            List<com.volcengine.ecs.model.NetworkInterfaceForRunInstancesInput> networkInterfaces = new ArrayList<>();
            networkInterfaces.add(networkInterface);

            // 构建公网IP配置
            com.volcengine.ecs.model.EipAddressForRunInstancesInput eipAddress = null;
            if (Boolean.TRUE.equals(request.getPublicIpEnabled())) {
                eipAddress = new com.volcengine.ecs.model.EipAddressForRunInstancesInput()
                    .bandwidthMbps(request.getBandwidth());
            }

            // 构建创建实例请求
            com.volcengine.ecs.model.RunInstancesRequest runRequest = 
                new com.volcengine.ecs.model.RunInstancesRequest()
                    .instanceName(request.getInstanceName())
                    .description(request.getDescription())
                    .instanceType(request.getInstanceType())
                    .imageId(request.getImageId())
                    .zoneId(request.getZoneId())
                    .keyPairName(request.getKeyPairName())
                    .volumes(volumes)
                    .networkInterfaces(networkInterfaces)
                    .count(request.getInstanceCount())
                    .instanceChargeType(request.getInstanceChargeType())
                    .eipAddress(eipAddress);

            // 调用API创建实例
            Object response = ecsApi.runInstances(runRequest);

            logger.info("ECS实例创建成功");
            return response;

        } catch (Exception e) {
            logger.error("创建ECS实例失败", e);
            throw new RuntimeException("创建ECS实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 启动ECS实例
     *
     * @param instanceId 实例ID
     * @return 启动结果
     */
    public Object startInstance(String instanceId) {
        logger.info("开始启动ECS实例: {}", instanceId);

        try {
            com.volcengine.ecs.model.StartInstancesRequest request = 
                new com.volcengine.ecs.model.StartInstancesRequest()
                    .instanceIds(java.util.Collections.singletonList(instanceId));

            Object response = ecsApi.startInstances(request);

            logger.info("ECS实例启动成功, 实例ID: {}", instanceId);
            return response;

        } catch (Exception e) {
            logger.error("启动ECS实例失败, 实例ID: {}", instanceId, e);
            throw new RuntimeException("启动ECS实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 停止ECS实例
     *
     * @param instanceId 实例ID
     * @param forceStop  是否强制停止
     * @return 停止结果
     */
    public Object stopInstance(String instanceId, boolean forceStop) {
        logger.info("开始停止ECS实例: {}, 强制停止: {}", instanceId, forceStop);

        try {
            com.volcengine.ecs.model.StopInstancesRequest request = 
                new com.volcengine.ecs.model.StopInstancesRequest()
                    .instanceIds(java.util.Collections.singletonList(instanceId))
                    .forceStop(forceStop);

            Object response = ecsApi.stopInstances(request);

            logger.info("ECS实例停止成功, 实例ID: {}", instanceId);
            return response;

        } catch (Exception e) {
            logger.error("停止ECS实例失败, 实例ID: {}", instanceId, e);
            throw new RuntimeException("停止ECS实例失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询ECS实例详情
     *
     * @param instanceId 实例ID
     * @return 实例详情
     */
    public Object describeInstance(String instanceId) {
        logger.info("查询ECS实例详情: {}", instanceId);

        try {
            com.volcengine.ecs.model.DescribeInstancesRequest request = 
                new com.volcengine.ecs.model.DescribeInstancesRequest()
                    .instanceIds(java.util.Collections.singletonList(instanceId));

            Object response = ecsApi.describeInstances(request);

            logger.info("查询ECS实例详情成功, 实例ID: {}", instanceId);
            return response;

        } catch (Exception e) {
            logger.error("查询ECS实例详情失败, 实例ID: {}", instanceId, e);
            throw new RuntimeException("查询ECS实例详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除ECS实例
     *
     * @param instanceId 实例ID
     * @return 删除结果
     */
    public Object deleteInstance(String instanceId) {
        logger.info("开始删除ECS实例: {}", instanceId);

        try {
            com.volcengine.ecs.model.DeleteInstanceRequest request = 
                new com.volcengine.ecs.model.DeleteInstanceRequest()
                    .instanceId(instanceId);

            Object response = ecsApi.deleteInstance(request);

            logger.info("ECS实例删除成功, 实例ID: {}", instanceId);
            return response;

        } catch (Exception e) {
            logger.error("删除ECS实例失败, 实例ID: {}", instanceId, e);
            throw new RuntimeException("删除ECS实例失败: " + e.getMessage(), e);
        }
    }
}