package com.gitegg.boot.extension.wx.pay.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;
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
@ApiModel(value="Pay对象", description="微信支付")
public class UpdatePayDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "微信名称")
    @NotBlank(message="微信名称不能为空")
    @Length(min=1,max=100)
    private String name;

    @ApiModelProperty(value = "appid")
    @NotBlank(message="appid不能为空")
    @Length(min=1,max=100)
    private String appId;

    @ApiModelProperty(value = "商户号")
    @NotBlank(message="商户号不能为空")
    @Length(min=1,max=100)
    private String mchId;

    @ApiModelProperty(value = "商户密钥")
    @Length(min=1,max=100)
    private String mchKey;

    @ApiModelProperty(value = "企业支付密钥")
    @Length(min=1,max=100)
    private String entPayKey;

    @ApiModelProperty(value = "子商户appid")
    @Length(min=1,max=100)
    private String subAppId;

    @ApiModelProperty(value = "子商户号")
    @Length(min=1,max=100)
    private String subMchId;

    @ApiModelProperty(value = "apiclient_cert.p12路径")
    @Length(min=1,max=100)
    private String keyPath;

    @ApiModelProperty(value = "serviceId")
    @Length(min=1,max=100)
    private String serviceId;

    @ApiModelProperty(value = "证书序列号")
    @Length(min=1,max=100)
    private String certSerialNo;

    @ApiModelProperty(value = "apiV3秘钥")
    @Length(min=1,max=100)
    private String apiV3Key;

    @ApiModelProperty(value = "支付分回调地址")
    @Length(min=1,max=100)
    private String payScoreNotifyUrl;

    @ApiModelProperty(value = "支付分授权回调地址")
    @Length(min=1,max=100)
    private String payScorePermissionNotifyUrl;

    @ApiModelProperty(value = "apiv3商户key")
    @Length(min=1,max=100)
    private String privateKeyPath;

    @ApiModelProperty(value = "apiv3商户cert")
    @Length(min=1,max=100)
    private String privateCertPath;

    @ApiModelProperty(value = "apiclient_key.pem")
    @Length(min=1,max=255)
    private String privateKeyContent;

    @ApiModelProperty(value = "piclient_cert.pem")
    @Length(min=1,max=255)
    private String privateCertContent;

    @ApiModelProperty(value = "签名方式")
    @Length(min=1,max=100)
    private String signType;

    @ApiModelProperty(value = "微信支付异步回掉地址")
    @Length(min=1,max=500)
    private String notifyUrl;

    @ApiModelProperty(value = "http请求数据读取等待时间")
    @Min(0L)
    @Max(2147483647L)
    private Integer httpTimeout;

    @ApiModelProperty(value = "http请求连接超时时间")
    @Min(0L)
    @Max(2147483647L)
    private Integer httpConnectionTimeout;

    @ApiModelProperty(value = "http_proxy_host")
    @Length(min=1,max=32)
    private String httpProxyHost;

    @ApiModelProperty(value = "http_proxy_port")
    @Length(min=1,max=100)
    private String httpProxyPort;

    @ApiModelProperty(value = "http_proxy_username")
    @Length(min=1,max=100)
    private String httpProxyUsername;

    @ApiModelProperty(value = "http_proxy_password")
    @Length(min=1,max=100)
    private String httpProxyPassword;

    @ApiModelProperty(value = "是否测试")
    private Boolean useSandboxEnv;

    @ApiModelProperty(value = "是否将接口请求日志信息保存到threadLocal中")
    private Boolean ifSaveApiData;

    @ApiModelProperty(value = "p12证书文件内容的字节数组")
    @Length(min=1,max=500)
    private String keyContent;

    @ApiModelProperty(value = "状态")
    @NotBlank(message="状态不能为空")
    @Length(min=1,max=32)
    private String status;

    @ApiModelProperty(value = "MD5")
    @Length(min=1,max=32)
    private String md5;

    @ApiModelProperty(value = "描述")
    @Length(min=1,max=255)
    private String comments;
}
