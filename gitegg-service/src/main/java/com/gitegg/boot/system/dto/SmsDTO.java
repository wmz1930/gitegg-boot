package com.gitegg.boot.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author gitegg
 */
@Data
@ApiModel(value = "SmsDTO对象", description = "发送短信对象")
public class SmsDTO {

    /**
     * 姓名
     */
    @Length(max = 8, min = 2, message = "姓名长度范围应该在2-8位之间。")
    @ApiModelProperty(value = "姓名", required = false, dataType = "String", notes = "姓名长度范围应该在2-8位之间。")
    private String realName;

    /**
     * 电话
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][1-9][0-9]{9}$", message = "请输入正确手机号。")
    @ApiModelProperty(value = "手机号", required = true, dataType = "String")
    private String mobile;

    /**
     * 图片验证码
     */
    @ApiModelProperty(value = "图片验证码")
    private String verKey;
}
