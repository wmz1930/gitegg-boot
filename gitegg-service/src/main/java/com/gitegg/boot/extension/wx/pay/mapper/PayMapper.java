package com.gitegg.boot.extension.wx.pay.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.gitegg.boot.extension.wx.miniapp.dto.MiniappDTO;
import com.gitegg.boot.extension.wx.miniapp.dto.QueryMiniappDTO;
import com.gitegg.boot.extension.wx.pay.entity.Pay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.boot.extension.wx.pay.dto.PayDTO;
import com.gitegg.boot.extension.wx.pay.dto.QueryPayDTO;

import java.util.List;

/**
 * <p>
 * 微信支付 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-29
 */
public interface PayMapper extends BaseMapper<Pay> {

    /**
    * 分页查询微信支付列表
    * @param page
    * @param payDTO
    * @return
    */
    Page<PayDTO> queryPayList(Page<PayDTO> page, @Param("pay") QueryPayDTO payDTO);

    /**
    * 查询微信支付列表
    * @param payDTO
    * @return
    */
    List<PayDTO> queryPayList(@Param("pay") QueryPayDTO payDTO);

    /**
    * 查询微信支付信息
    * @param payDTO
    * @return
    */
    PayDTO queryPay(@Param("pay") QueryPayDTO payDTO);

    /**
     * 排除多租户插件查询微信支付配置列表
     * @param payDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<PayDTO> initWxPayList(@Param("pay") QueryPayDTO payDTO);

    /**
     * 排除多租户插件查询微信支付配置信息
     * @param payDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    PayDTO getWxPay(@Param("pay") QueryPayDTO payDTO);
}
