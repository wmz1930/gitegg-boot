package com.gitegg.boot.extension.wx.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@TableName("t_wechat_pay")
@ApiModel(value = "Pay对象", description = "微信支付")
public class Pay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "微信名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "微信appid")
    @TableField("app_id")
    private String appId;

    @ApiModelProperty(value = "微信支付商户号")
    @TableField("mch_id")
    private String mchId;

    @ApiModelProperty(value = "微信支付商户密钥")
    @TableField("mch_key")
    private String mchKey;

    @ApiModelProperty(value = "企业支付密钥")
    @TableField("ent_pay_key")
    private String entPayKey;

    @ApiModelProperty(value = "子商户微信appid")
    @TableField("sub_app_id")
    private String subAppId;

    @ApiModelProperty(value = "子商户号")
    @TableField("sub_mch_id")
    private String subMchId;

    @ApiModelProperty(value = "apiclient_cert.p12文件的绝对路径")
    @TableField("key_path")
    private String keyPath;

    @ApiModelProperty(value = "微信支付分serviceId")
    @TableField("service_id")
    private String serviceId;

    @ApiModelProperty(value = "证书序列号")
    @TableField("cert_serial_no")
    private String certSerialNo;

    @ApiModelProperty(value = "apiV3秘钥")
    @TableField("api_v3_key")
    private String apiV3Key;

    @ApiModelProperty(value = "微信支付分回调地址")
    @TableField("pay_score_notify_url")
    private String payScoreNotifyUrl;

    @ApiModelProperty(value = "微信支付分授权回调地址")
    @TableField("pay_score_permission_notify_url")
    private String payScorePermissionNotifyUrl;

    @ApiModelProperty(value = "apiv3商户apiclient_key.pem")
    @TableField("private_key_path")
    private String privateKeyPath;

    @ApiModelProperty(value = "apiv3商户apiclient_cert.pem")
    @TableField("private_cert_path")
    private String privateCertPath;

    @ApiModelProperty(value = "apiclient_key.pem证书文件内容的字节数组")
    @TableField("private_key_content")
    private String privateKeyContent;

    @ApiModelProperty(value = "piclient_cert.pem证书文件内容的字节数组")
    @TableField("private_cert_content")
    private String privateCertContent;

    @ApiModelProperty(value = "签名方式 HMAC_SHA256 和MD5")
    @TableField("sign_type")
    private String signType;

    @ApiModelProperty(value = "微信支付异步回掉地址，通知url必须为直接可访问的url，不能携带参数.")
    @TableField("notify_url")
    private String notifyUrl;

    @ApiModelProperty(value = "http请求数据读取等待时间")
    @TableField("http_timeout")
    private Integer httpTimeout;

    @ApiModelProperty(value = "http请求连接超时时间")
    @TableField("http_connection_timeout")
    private Integer httpConnectionTimeout;

    @ApiModelProperty(value = "http_proxy_host")
    @TableField("http_proxy_host")
    private String httpProxyHost;

    @ApiModelProperty(value = "http_proxy_port")
    @TableField("http_proxy_port")
    private String httpProxyPort;

    @ApiModelProperty(value = "http_proxy_username")
    @TableField("http_proxy_username")
    private String httpProxyUsername;

    @ApiModelProperty(value = "http_proxy_password")
    @TableField("http_proxy_password")
    private String httpProxyPassword;

    @ApiModelProperty(value = "微信支付是否使用仿真测试环境")
    @TableField("use_sandbox_env")
    private Boolean useSandboxEnv;

    @ApiModelProperty(value = "是否将接口请求日志信息保存到threadLocal中")
    @TableField("if_save_api_data")
    private Boolean ifSaveApiData;

    @ApiModelProperty(value = "p12证书文件内容的字节数组")
    @TableField("key_content")
    private String keyContent;

    @ApiModelProperty(value = "状态 1有效 0禁用")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "MD5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty(value = "描述")
    @TableField("comments")
    private String comments;

}