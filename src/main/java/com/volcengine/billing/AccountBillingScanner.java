package com.volcengine.billing;

import com.volcengine.ApiClient;
import com.volcengine.ApiException;
import com.volcengine.billing.model.QueryBalanceAcctRequest;
import com.volcengine.billing.model.QueryBalanceAcctResponse;
import com.volcengine.common.ProfileConfig;
import com.volcengine.sign.Credentials;

import java.util.Properties;

/**
 * 账户余额查询工具类 (增加详细错误诊断)
 */
public class AccountBillingScanner {

    private static Properties credentials;

    public static void main(String[] args) {
        try {
            credentials = ProfileConfig.load();

            if (credentials == null || credentials.isEmpty()) {
                System.err.println("错误: 无法加载配置文件");
                return;
            }

            String ak = credentials.getProperty("volc.access.key.id");
            String sk = credentials.getProperty("volc.access.key.secret");

            if (ak == null || sk == null) {
                System.err.println("错误: 配置文件中缺少 AccessKey 信息");
                return;
            }

            // 1. 初始化 ApiClient
            ApiClient apiClient = new ApiClient()
                    .setCredentials(Credentials.getCredentials(ak, sk))
                    .setRegion("cn-beijing");

            // 2. 创建 Billing API 实例
            BillingApi billingApi = new BillingApi(apiClient);

            System.out.println("--- 正在查询火山引擎账户余额 (OpenAPI 版) ---");

            // 3. 构造请求
            QueryBalanceAcctRequest request = new QueryBalanceAcctRequest();
            
            // 4. 发起调用
            try {
                QueryBalanceAcctResponse response = billingApi.queryBalanceAcct(request);
                
                // 5. 解析结果
                if (response != null) {
                    String cashBalance = response.getCashBalance();
                    String availableBalance = response.getAvailableBalance();
                    String freezeAmount = response.getFreezeAmount();
                    
                    System.out.println("\n================================");
                    System.out.println("【 火山引擎账户查账报告 】");
                    System.out.println("--------------------------------");
                    System.out.println("可用现金余额: ¥ " + (cashBalance != null ? cashBalance : "0.00"));
                    System.out.println("当前冻结金额: ¥ " + (freezeAmount != null ? freezeAmount : "0.00"));
                    System.out.println("账户总可用额: ¥ " + (availableBalance != null ? availableBalance : "0.00"));
                    System.out.println("--------------------------------");
                    System.out.println("💡 提示：总可用额 = 现金 + 代金券 - 冻结");
                    
                    if (cashBalance != null) {
                        double balance = Double.parseDouble(cashBalance.replace(",", ""));
                        if (balance < 200) {
                            System.err.println("⚠️ 风险提示：当前余额低于 ¥200！");
                        } else {
                            System.out.println("✅ 状态：余额充足。");
                        }
                    }
                    System.out.println("================================\n");
                }
            } catch (ApiException e) {
                System.err.println("API 调用失败!");
                System.err.println("状态码: " + e.getCode());
                System.err.println("错误详情: " + e.getResponseBody());
            }

            // 5. 尝试优雅关闭 OkHttp 线程池 (解决 Maven exec:java 运行后的线程残留警告)
            try {
                if (apiClient.getHttpClient() != null) {
                    apiClient.getHttpClient().getDispatcher().getExecutorService().shutdown();
                    if (apiClient.getHttpClient().getConnectionPool() != null) {
                        apiClient.getHttpClient().getConnectionPool().evictAll();
                    }
                }
            } catch (Exception e) {
                // 忽略关闭时的异常
            }
            
            // 强行退出
            System.exit(0);
            
        } catch (Exception e) {
            System.err.println("程序执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
