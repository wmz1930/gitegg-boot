package com.gitegg.boot.extension.wx.pay.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 微信支付
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-29
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="Pay对象", description="微信支付数据导出")
public class PayExportBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信名称")
    @ExcelProperty(value = "微信名称" ,index = 0)
    @ColumnWidth(20)
    private String name;

    @ApiModelProperty(value = "appid")
    @ExcelProperty(value = "appid" ,index = 1)
    @ColumnWidth(20)
    private String appId;

    @ApiModelProperty(value = "商户号")
    @ExcelProperty(value = "商户号" ,index = 2)
    @ColumnWidth(20)
    private String mchId;

    @ApiModelProperty(value = "商户密钥")
    @ExcelProperty(value = "商户密钥" ,index = 3)
    @ColumnWidth(20)
    private String mchKey;

    @ApiModelProperty(value = "企业支付密钥")
    @ExcelProperty(value = "企业支付密钥" ,index = 4)
    @ColumnWidth(20)
    private String entPayKey;

    @ApiModelProperty(value = "子商户appid")
    @ExcelProperty(value = "子商户appid" ,index = 5)
    @ColumnWidth(20)
    private String subAppId;

    @ApiModelProperty(value = "子商户号")
    @ExcelProperty(value = "子商户号" ,index = 6)
    @ColumnWidth(20)
    private String subMchId;

    @ApiModelProperty(value = "apiclient_cert.p12路径")
    @ExcelProperty(value = "apiclient_cert.p12路径" ,index = 7)
    @ColumnWidth(20)
    private String keyPath;

    @ApiModelProperty(value = "serviceId")
    @ExcelProperty(value = "serviceId" ,index = 8)
    @ColumnWidth(20)
    private String serviceId;

    @ApiModelProperty(value = "证书序列号")
    @ExcelProperty(value = "证书序列号" ,index = 9)
    @ColumnWidth(20)
    private String certSerialNo;

    @ApiModelProperty(value = "apiV3秘钥")
    @ExcelProperty(value = "apiV3秘钥" ,index = 10)
    @ColumnWidth(20)
    private String apiV3Key;

    @ApiModelProperty(value = "支付分回调地址")
    @ExcelProperty(value = "支付分回调地址" ,index = 11)
    @ColumnWidth(20)
    private String payScoreNotifyUrl;

    @ApiModelProperty(value = "支付分授权回调地址")
    @ExcelProperty(value = "支付分授权回调地址" ,index = 12)
    @ColumnWidth(20)
    private String payScorePermissionNotifyUrl;

    @ApiModelProperty(value = "apiv3商户key")
    @ExcelProperty(value = "apiv3商户key" ,index = 13)
    @ColumnWidth(20)
    private String privateKeyPath;

    @ApiModelProperty(value = "apiv3商户cert")
    @ExcelProperty(value = "apiv3商户cert" ,index = 14)
    @ColumnWidth(20)
    private String privateCertPath;

    @ApiModelProperty(value = "apiclient_key.pem")
    @ExcelProperty(value = "apiclient_key.pem" ,index = 15)
    @ColumnWidth(20)
    private String privateKeyContent;

    @ApiModelProperty(value = "piclient_cert.pem")
    @ExcelProperty(value = "piclient_cert.pem" ,index = 16)
    @ColumnWidth(20)
    private String privateCertContent;

    @ApiModelProperty(value = "签名方式")
    @ExcelProperty(value = "签名方式" ,index = 17)
    @ColumnWidth(20)
    private String signType;

    @ApiModelProperty(value = "微信支付异步回掉地址")
    @ExcelProperty(value = "微信支付异步回掉地址" ,index = 18)
    @ColumnWidth(20)
    private String notifyUrl;

    @ApiModelProperty(value = "http请求数据读取等待时间")
    @ExcelProperty(value = "http请求数据读取等待时间" ,index = 19)
    @ColumnWidth(20)
    private Integer httpTimeout;

    @ApiModelProperty(value = "http请求连接超时时间")
    @ExcelProperty(value = "http请求连接超时时间" ,index = 20)
    @ColumnWidth(20)
    private Integer httpConnectionTimeout;

    @ApiModelProperty(value = "http_proxy_host")
    @ExcelProperty(value = "http_proxy_host" ,index = 21)
    @ColumnWidth(20)
    private String httpProxyHost;

    @ApiModelProperty(value = "http_proxy_port")
    @ExcelProperty(value = "http_proxy_port" ,index = 22)
    @ColumnWidth(20)
    private String httpProxyPort;

    @ApiModelProperty(value = "http_proxy_username")
    @ExcelProperty(value = "http_proxy_username" ,index = 23)
    @ColumnWidth(20)
    private String httpProxyUsername;

    @ApiModelProperty(value = "http_proxy_password")
    @ExcelProperty(value = "http_proxy_password" ,index = 24)
    @ColumnWidth(20)
    private String httpProxyPassword;

    @ApiModelProperty(value = "是否测试")
    @ExcelProperty(value = "是否测试" ,index = 25)
    @ColumnWidth(20)
    private Boolean useSandboxEnv;

    @ApiModelProperty(value = "是否将接口请求日志信息保存到threadLocal中")
    @ExcelProperty(value = "是否将接口请求日志信息保存到threadLocal中" ,index = 26)
    @ColumnWidth(20)
    private Boolean ifSaveApiData;

    @ApiModelProperty(value = "p12证书文件内容的字节数组")
    @ExcelProperty(value = "p12证书文件内容的字节数组" ,index = 27)
    @ColumnWidth(20)
    private String keyContent;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 28)
    @ColumnWidth(20)
    private String status;

    @ApiModelProperty(value = "MD5")
    @ExcelProperty(value = "MD5" ,index = 29)
    @ColumnWidth(20)
    private String md5;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 30)
    @ColumnWidth(20)
    private String comments;
}
