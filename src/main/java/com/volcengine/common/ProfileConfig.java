package com.volcengine.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ProfileConfig {

    private static final String[] VALID_PROFILES = {"personal", "enterprise", "client"};
    private static final String DEFAULT_PROFILE = "personal";

    public static String getProfile() {
        String profile = System.getenv("PROFILE");
        if (profile == null || profile.isEmpty()) {
            profile = System.getProperty("profile", DEFAULT_PROFILE);
        }
        for (String valid : VALID_PROFILES) {
            if (valid.equals(profile)) {
                return profile;
            }
        }
        throw new IllegalArgumentException("无效的 PROFILE: " + profile + "，可选值: " + String.join(", ", VALID_PROFILES));
    }

    public static Properties load() {
        String profile = getProfile();
        String projectDir = System.getProperty("user.dir");
        String fileName = "credentials-" + profile + ".properties";
        String filePath = projectDir + File.separator + fileName;

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
            System.out.println("[配置] 已加载 Profile: " + profile + " (" + filePath + ")");
            return props;
        } catch (IOException e) {
            System.err.println("错误: 无法加载配置文件: " + filePath);
            System.err.println("请创建 " + fileName + " 文件，可参考 credentials-personal.properties 模板");
            return null;
        }
    }
}
