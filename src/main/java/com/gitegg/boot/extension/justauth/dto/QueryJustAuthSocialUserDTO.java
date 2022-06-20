package com.gitegg.boot.extension.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 第三方用户绑定
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSocialUser对象", description="第三方用户绑定")
public class QueryJustAuthSocialUserDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "用户id")
    @Length(min=1,max=19)
    private Long userId;
    
    @ApiModelProperty(value = "第三方用户id")
    @Length(min=1,max=19)
    private Long socialId;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
