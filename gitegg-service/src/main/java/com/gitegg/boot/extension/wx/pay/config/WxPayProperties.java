package com.gitegg.boot.extension.wx.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author GitEgg
 * @date 2023/7/29
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    private List<PayConfig> configs;

    @Data
    public static class PayConfig {

        /**
         * 租户
         */
        private Long tenantId;

        /**
         * 设置微信公众号或者小程序等的appid.
         */
        private String appId;

        /**
         * 微信支付商户号.
         */
        private String mchId;

        /**
         * 微信支付商户密钥.
         */
        private String mchKey;

        /**
         * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除.
         */
        private String subAppId;

        /**
         * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除.
         */
        private String subMchId;

        /**
         * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定.
         */
        private String keyPath;

        /**
         * 微信支付分serviceId
         */
        private String serviceId;

        /**
         * 证书序列号
         */
        private String certSerialNo;

        /**
         * apiV3秘钥
         */
        private String apiV3Key;

        /**
         * 微信支付分回调地址
         */
        private String payScoreNotifyUrl;

        /**
         * apiv3 商户apiclient_key.pem
         */
        private String privateKeyPath;

        /**
         * apiv3 商户apiclient_cert.pem
         */
        private String privateCertPath;
    }
}
