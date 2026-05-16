package com.volcengine.ecs;

import com.volcengine.ecs.client.ECSClient;
import com.volcengine.ecs.config.VolcEngineConfig;
import com.volcengine.ecs.model.ECSInstanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
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
        loadCredentials();

        if (credentials == null || credentials.isEmpty()) {
            System.out.println("错误: 无法加载 credentials.properties 配置文件");
            System.out.println("请确保 credentials.properties 文件存在于项目根目录");
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
            System.out.println("错误: credentials.properties 中缺少 AccessKey 配置");
            return;
        }

        VolcEngineConfig config = new VolcEngineConfig();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.setRegionId(REGION_ID);

        ECSClient ecsClient = new ECSClient(config);

        testCreateInstance(ecsClient, imageId, vpcId, subnetId, keyPairName, securityGroupId);
    }

    private static void loadCredentials() {
        credentials = new Properties();
        try (FileInputStream fis = new FileInputStream("credentials.properties")) {
            credentials.load(fis);
            System.out.println("成功加载 credentials.properties 配置文件");
        } catch (IOException e) {
            System.out.println("警告: 无法读取 credentials.properties 文件: " + e.getMessage());
            credentials = null;
        }
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