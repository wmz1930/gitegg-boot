package com.gitegg.boot.extension.wx.pay.service;

import java.util.List;

import com.gitegg.boot.extension.wx.pay.entity.Pay;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.boot.extension.wx.pay.bo.PayExportBO;
import com.gitegg.boot.extension.wx.pay.dto.PayDTO;
import com.gitegg.boot.extension.wx.pay.dto.CreatePayDTO;
import com.gitegg.boot.extension.wx.pay.dto.UpdatePayDTO;
import com.gitegg.boot.extension.wx.pay.dto.QueryPayDTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 微信支付 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-29
 */
public interface IPayService extends IService<Pay> {

    /**
    * 分页查询微信支付列表
    * @param page
    * @param queryPayDTO
    * @return
    */
    Page<PayDTO> queryPayList(Page<PayDTO> page, QueryPayDTO queryPayDTO);

    /**
    * 查询微信支付列表
    * @param queryPayDTO
    * @return
    */
    List<PayDTO> queryPayList(QueryPayDTO queryPayDTO);

    /**
    * 查询微信支付详情
    * @param queryPayDTO
    * @return
    */
    PayDTO queryPay(QueryPayDTO queryPayDTO);

    /**
    * 创建微信支付
    * @param pay
    * @return
    */
    boolean createPay(CreatePayDTO pay);

    /**
    * 更新微信支付
    * @param pay
    * @return
    */
    boolean updatePay(UpdatePayDTO pay);

    /**
    * 删除微信支付
    * @param payId
    * @return
    */
    boolean deletePay(Long payId);

    /**
    * 批量删除微信支付
    * @param payIds
    * @return
    */
    boolean batchDeletePay(List<Long> payIds);

    /**
    * 导出微信支付列表
    * @param queryPayDTO
    * @return
    */
    List<PayExportBO> exportPayList(QueryPayDTO queryPayDTO);

    /**
    * 导入微信支付列表
    * @param file
    * @return
    */
    boolean importPayList(MultipartFile file);

    /**
     * 初始化微信支付配置表列表
     * @return
     */
    void initWxPayList();

    /**
     * 通过appid获取租户id，忽略租户插件
     * @param payDTO
     * @return
     */
    PayDTO getWxPay(QueryPayDTO payDTO);

    /**
     * 通过appid获取appid，忽略租户插件
     * @param appId
     * @return
     */
    String getWxPayAppId(String appId);

    /**
     * 根据AppId切换当前ThreadLocal的微信支付
     * @param appId
     * @return
     */
    boolean switchover(String appId);
}
