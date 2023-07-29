package com.gitegg.boot.extension.wx.pay.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.util.List;
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
public class QueryPayDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信名称")
    private String name;

    @ApiModelProperty(value = "appid")
    private String appId;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "批量查询")
    private List<Long> payIds;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}