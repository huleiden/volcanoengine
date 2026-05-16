package com.volcengine.ecs;

import com.volcengine.ecs.client.ECSClient;
import com.volcengine.ecs.config.VolcEngineConfig;
import com.volcengine.ecs.model.ECSInstanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Scanner;

public class ECSInstanceCreator {

    private static final Logger logger = LoggerFactory.getLogger(ECSInstanceCreator.class);

    private static final String ACCESS_KEY_ID = System.getenv("VOLC_ACCESS_KEY_ID");
    private static final String ACCESS_KEY_SECRET = System.getenv("VOLC_ACCESS_KEY_SECRET");
    private static final String REGION_ID = "cn-shanghai";
    private static final String ZONE_ID = "cn-shanghai-a";
    private static final String IMAGE_ID = System.getenv("VOLC_IMAGE_ID");
    private static final String VPC_ID = System.getenv("VOLC_VPC_ID");
    private static final String SUBNET_ID = System.getenv("VOLC_SUBNET_ID");
    private static final String KEY_PAIR_NAME = System.getenv("VOLC_KEY_PAIR_NAME");
    private static final String SECURITY_GROUP_ID = System.getenv("VOLC_SECURITY_GROUP_ID");

    public static void main(String[] args) {
        if (ACCESS_KEY_ID == null || ACCESS_KEY_SECRET == null) {
            System.out.println("错误: 请设置环境变量 VOLC_ACCESS_KEY_ID 和 VOLC_ACCESS_KEY_SECRET");
            System.out.println("示例: export VOLC_ACCESS_KEY_ID=your_access_key_id");
            System.out.println("示例: export VOLC_ACCESS_KEY_SECRET=your_access_key_secret");
            return;
        }

        VolcEngineConfig config = new VolcEngineConfig();
        config.setAccessKeyId(ACCESS_KEY_ID);
        config.setAccessKeySecret(ACCESS_KEY_SECRET);
        config.setRegionId(REGION_ID);

        ECSClient ecsClient = new ECSClient(config);

        testCreateInstance(ecsClient);
    }

    private static void testCreateInstance(ECSClient ecsClient) {
        System.out.println("\n--- 创建ECS实例 ---");

        ECSInstanceRequest request = new ECSInstanceRequest()
                .setInstanceName("test-ecs-" + System.currentTimeMillis())
                .setDescription("测试ECS实例 - 通过Java SDK创建")
                .setInstanceType("ecs.e-c1m1.large")
                .setImageId(IMAGE_ID)
                .setVpcId(VPC_ID)
                .setSubnetId(SUBNET_ID)
                .setZoneId(ZONE_ID)
                .setKeyPairName(KEY_PAIR_NAME)
                .setSystemDiskType("ESSD_PL0")
                .setSystemDiskSize(40)
                .setInstanceCount(1)
                .setSecurityGroupIds(Arrays.asList(SECURITY_GROUP_ID))
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

    public static void testFullWorkflow(ECSClient ecsClient) {
        try {
            System.out.println("\n========== 完整流程测试 ==========");

            System.out.println("\n步骤1: 创建ECS实例...");
            ECSInstanceRequest request = new ECSInstanceRequest()
                    .setInstanceName("workflow-test-" + System.currentTimeMillis())
                    .setDescription("完整流程测试实例")
                    .setInstanceType("ecs.e-c1m1.large")
                    .setImageId(IMAGE_ID)
                    .setVpcId(VPC_ID)
                    .setSubnetId(SUBNET_ID)
                    .setKeyPairName(KEY_PAIR_NAME)
                    .setSystemDiskType("ESSD_PL0")
                    .setSystemDiskSize(40)
                    .setInstanceCount(1)
                    .setSecurityGroupIds(Arrays.asList(SECURITY_GROUP_ID))
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