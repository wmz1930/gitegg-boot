package com.gitegg.boot.extension.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
public class UpdateJustAuthSocialUserDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long userId;

    @ApiModelProperty(value = "第三方用户id")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long socialId;
}
