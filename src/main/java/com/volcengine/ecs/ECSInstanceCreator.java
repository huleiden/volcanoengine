package com.volcengine.ecs;

import com.volcengine.ecs.client.ECSClient;
import com.volcengine.ecs.config.VolcEngineConfig;
import com.volcengine.ecs.model.ECSInstanceRequest;
import com.volcengine.common.ProfileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class ECSInstanceCreator {

    private static final Logger logger = LoggerFactory.getLogger(ECSInstanceCreator.class);

    private static final String REGION_ID = "cn-shanghai";
    private static final String ZONE_ID = "cn-shanghai-a";
    private static final String INSTANCE_TYPE = "ecs.e-c1m1.large";

    private static Properties credentials;

    public static void main(String[] args) {
        credentials = ProfileConfig.load();

        if (credentials == null || credentials.isEmpty()) {
            System.out.println("错误: 无法加载配置文件");
            return;
        }

        String accessKeyId = credentials.getProperty("volc.access.key.id");
        String accessKeySecret = credentials.getProperty("volc.access.key.secret");
        String imageId = credentials.getProperty("volc.image.id");
        String vpcId = credentials.getProperty("volc.vpc.id");
        String subnetId = credentials.getProperty("volc.subnet.id");
        String keyPairName = credentials.getProperty("volc.key.pair.name");
        String securityGroupId = credentials.getProperty("volc.security.group.id");

        if (accessKeyId == null || accessKeySecret == null) {
            System.out.println("错误: 配置文件中缺少 AccessKey 信息");
            return;
        }

        VolcEngineConfig config = new VolcEngineConfig();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.setRegionId(REGION_ID);

        ECSClient ecsClient = new ECSClient(config);

        // 1. 测试创建实例
        // testCreateInstance(ecsClient, imageId, vpcId, subnetId, keyPairName, securityGroupId);

        // 2. 测试查询实例 (硬编码刚才创建的实例ID)
        String instanceId = "i-yemc0tyfi8qbxythykq1";
        // System.out.println("\n--- 查询ECS实例: " + instanceId + " ---");
        // Object response = ecsClient.describeInstance(instanceId);
        // System.out.println("查询响应: " + response);

        // 3. 测试停止实例
        // System.out.println("\n--- 停止ECS实例: " + instanceId + " ---");
        // Object stopResponse = ecsClient.stopInstance(instanceId, false);
        // System.out.println("停止响应: " + stopResponse);

        // 4. 测试启动实例
        // System.out.println("\n--- 启动ECS实例: " + instanceId + " ---");
        // Object startResponse = ecsClient.startInstance(instanceId);
        // System.out.println("启动响应: " + startResponse);

        // 5. 测试删除实例
        System.out.println("\n--- 删除ECS实例: " + instanceId + " ---");
        Object deleteResponse = ecsClient.deleteInstance(instanceId);
        System.out.println("删除响应: " + deleteResponse);

        // 6. 测试完整工作流 (创建 + 等待 + 查询)
        // testFullWorkflow(ecsClient, imageId, vpcId, subnetId, keyPairName, securityGroupId);
    }

    private static void testCreateInstance(ECSClient ecsClient, String imageId, String vpcId,
                                          String subnetId, String keyPairName, String securityGroupId) {
        System.out.println("\n--- 创建ECS实例 ---");

        ECSInstanceRequest request = new ECSInstanceRequest()
                .setInstanceName("test-ecs-" + System.currentTimeMillis())
                .setDescription("测试ECS实例 - 通过Java SDK创建")
                .setInstanceType(INSTANCE_TYPE)
                .setImageId(imageId)
                .setVpcId(vpcId)
                .setSubnetId(subnetId)
                .setZoneId(ZONE_ID)
                .setKeyPairName(keyPairName)
                .setSystemDiskType("ESSD_PL0")
                .setSystemDiskSize(40)
                .setInstanceCount(1)
                .setSecurityGroupIds(Arrays.asList(securityGroupId))
                .setInstanceChargeType("PostPaid")
                .setPublicIpEnabled(true)
                .setBandwidth(1);

        Object response = ecsClient.createInstance(request);
        System.out.println("创建响应: " + response);
    }

    private static void testDescribeInstance(ECSClient ecsClient, Scanner scanner) {
        System.out.println("\n--- 查询ECS实例 ---");
        System.out.print("请输入实例ID (输入 'q' 退出): ");
        String instanceId = scanner.nextLine();

        if ("q".equalsIgnoreCase(instanceId)) {
            return;
        }

        Object response = ecsClient.describeInstance(instanceId);
        System.out.println("查询响应: " + response);
    }

    public static void testFullWorkflow(ECSClient ecsClient, String imageId, String vpcId,
                                       String subnetId, String keyPairName, String securityGroupId) {
        try {
            System.out.println("\n========== 完整流程测试 ==========");

            System.out.println("\n步骤1: 创建ECS实例...");
            ECSInstanceRequest request = new ECSInstanceRequest()
                    .setInstanceName("workflow-test-" + System.currentTimeMillis())
                    .setDescription("完整流程测试实例")
                    .setInstanceType(INSTANCE_TYPE)
                    .setImageId(imageId)
                    .setVpcId(vpcId)
                    .setSubnetId(subnetId)
                    .setKeyPairName(keyPairName)
                    .setSystemDiskType("ESSD_PL0")
                    .setSystemDiskSize(40)
                    .setInstanceCount(1)
                    .setSecurityGroupIds(Arrays.asList(securityGroupId))
                    .setInstanceChargeType("PostPaid")
                    .setPublicIpEnabled(true)
                    .setBandwidth(1);

            Object createResponse = ecsClient.createInstance(request);
            System.out.println("创建成功: " + createResponse);

            Thread.sleep(10000);

            System.out.println("\n步骤2: 查询实例状态...");
            String instanceId = extractInstanceId(createResponse);
            if (instanceId != null) {
                Object describeResponse = ecsClient.describeInstance(instanceId);
                System.out.println("实例状态: " + describeResponse);
            }

            System.out.println("\n========== 测试完成 ==========");
        } catch (Exception e) {
            logger.error("完整流程测试失败", e);
        }
    }

    private static String extractInstanceId(Object response) {
        return null;
    }
}
