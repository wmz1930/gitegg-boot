package com.gitegg.boot.extension.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@Data
@TableName("t_sys_district")
@ApiModel(value = "District对象", description = "")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("district_name")
    private String districtName;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("initial")
    private String initial;

    @TableField("initials")
    private String initials;

    @TableField("pinyin")
    private String pinyin;

    @TableField("suffix")
    private String suffix;

    @TableField("code")
    private String code;

    @TableField("district_order")
    private Integer districtOrder;

    @TableField(exist = false)
    private List<District> children = new ArrayList<>();
}
