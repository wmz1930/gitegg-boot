package com.gitegg.boot.extension.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 微信支付
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PayDTO对象", description="微信支付")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"createTime", "updateTime"}, ignoreUnknown = true)
public class PayDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "微信名称")
    private String name;

    @ApiModelProperty(value = "appid")
    private String appId;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "商户密钥")
    private String mchKey;

    @ApiModelProperty(value = "企业支付密钥")
    private String entPayKey;

    @ApiModelProperty(value = "子商户appid")
    private String subAppId;

    @ApiModelProperty(value = "子商户号")
    private String subMchId;

    @ApiModelProperty(value = "apiclient_cert.p12路径")
    private String keyPath;

    @ApiModelProperty(value = "serviceId")
    private String serviceId;

    @ApiModelProperty(value = "证书序列号")
    private String certSerialNo;

    @ApiModelProperty(value = "apiV3秘钥")
    private String apiV3Key;

    @ApiModelProperty(value = "支付分回调地址")
    private String payScoreNotifyUrl;

    @ApiModelProperty(value = "支付分授权回调地址")
    private String payScorePermissionNotifyUrl;

    @ApiModelProperty(value = "apiv3商户key")
    private String privateKeyPath;

    @ApiModelProperty(value = "apiv3商户cert")
    private String privateCertPath;

    @ApiModelProperty(value = "apiclient_key.pem")
    private String privateKeyContent;

    @ApiModelProperty(value = "piclient_cert.pem")
    private String privateCertContent;

    @ApiModelProperty(value = "签名方式")
    private String signType;

    @ApiModelProperty(value = "微信支付异步回掉地址")
    private String notifyUrl;

    @ApiModelProperty(value = "http请求数据读取等待时间")
    private Integer httpTimeout;

    @ApiModelProperty(value = "http请求连接超时时间")
    private Integer httpConnectionTimeout;

    @ApiModelProperty(value = "http_proxy_host")
    private String httpProxyHost;

    @ApiModelProperty(value = "http_proxy_port")
    private String httpProxyPort;

    @ApiModelProperty(value = "http_proxy_username")
    private String httpProxyUsername;

    @ApiModelProperty(value = "http_proxy_password")
    private String httpProxyPassword;

    @ApiModelProperty(value = "是否测试")
    private Boolean useSandboxEnv;

    @ApiModelProperty(value = "是否将接口请求日志信息保存到threadLocal中")
    private Boolean ifSaveApiData;

    @ApiModelProperty(value = "p12证书文件内容的字节数组")
    private String keyContent;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "MD5")
    private String md5;

    @ApiModelProperty(value = "描述")
    private String comments;
}