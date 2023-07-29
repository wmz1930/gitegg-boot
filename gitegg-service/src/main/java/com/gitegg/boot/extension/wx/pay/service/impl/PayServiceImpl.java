package com.gitegg.boot.extension.wx.pay.service.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.extension.wx.pay.bo.PayExportBO;
import com.gitegg.boot.extension.wx.pay.bo.PayImportBO;
import com.gitegg.boot.extension.wx.pay.dto.CreatePayDTO;
import com.gitegg.boot.extension.wx.pay.dto.PayDTO;
import com.gitegg.boot.extension.wx.pay.dto.QueryPayDTO;
import com.gitegg.boot.extension.wx.pay.dto.UpdatePayDTO;
import com.gitegg.boot.extension.wx.pay.entity.Pay;
import com.gitegg.boot.extension.wx.pay.mapper.PayMapper;
import com.gitegg.boot.extension.wx.pay.service.IPayService;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.platform.wechat.pay.config.GitEggWxPayConfig;
import com.gitegg.platform.wechat.pay.constant.WeChatPayConstant;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信支付 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-29
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements IPayService {

    private final PayMapper payMapper;

    private final RedisTemplate redisTemplate;

    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

    /**
     * 解决循环依赖问题
     */
    private WxPayService wxPayService;

    @Autowired
    public void setWxPayService(@Lazy WxPayService wxPayService) {
        this.wxPayService = wxPayService;
    }

    /**
    * 分页查询微信支付列表
    * @param page
    * @param queryPayDTO
    * @return
    */
    @Override
    public Page<PayDTO> queryPayList(Page<PayDTO> page, QueryPayDTO queryPayDTO) {
        Page<PayDTO> payInfoList = payMapper.queryPayList(page, queryPayDTO);
        return payInfoList;
    }

    /**
    * 查询微信支付列表
    * @param queryPayDTO
    * @return
    */
    @Override
    public List<PayDTO> queryPayList(QueryPayDTO queryPayDTO) {
        List<PayDTO> payInfoList = payMapper.queryPayList(queryPayDTO);
        return payInfoList;
    }

    /**
    * 查询微信支付详情
    * @param queryPayDTO
    * @return
    */
    @Override
    public PayDTO queryPay(QueryPayDTO queryPayDTO) {
        PayDTO payDTO = payMapper.queryPay(queryPayDTO);
        return payDTO;
    }

    /**
    * 创建微信支付
    * @param pay
    * @return
    */
    @Override
    public boolean createPay(CreatePayDTO pay) {

        Pay payEntity = BeanCopierUtils.copyByClass(pay, Pay.class);
        try {
            String payEntityStr = JsonUtils.objToJson(payEntity);
            payEntity.setMd5(SecureUtil.md5(payEntityStr));
        } catch (Exception e) {
            log.error("创建微信支付配置时，md5加密失败:{}", e);
            throw new BusinessException("创建微信支付配置时，md5加密失败:" + e);
        }
        boolean result = this.save(payEntity);
        if (result)
        {
            // 更新到缓存
            Pay payEntityLocal = this.getById(payEntity.getId());
            PayDTO payDTO = BeanCopierUtils.copyByClass(payEntityLocal, PayDTO.class);
            this.addOrUpdateWxPayCache(payDTO);
        }
        return result;
    }

    /**
    * 更新微信支付
    * @param pay
    * @return
    */
    @Override
    public boolean updatePay(UpdatePayDTO pay) {

        Pay payEntity = BeanCopierUtils.copyByClass(pay, Pay.class);
        Pay payEntityOld = this.getById(payEntity.getId());
        try {
            String payEntityStr = JsonUtils.objToJson(payEntity);
            payEntity.setMd5(SecureUtil.md5(payEntityStr));
        } catch (Exception e) {
            log.error("创建微信小程序配置时，md5加密失败:{}", e);
            throw new BusinessException("创建微信小程序配置时，md5加密失败:" + e);
        }
        boolean result = this.updateById(payEntity);
        if (result)
        {
            // 把旧的删掉
            PayDTO payDTOOld = BeanCopierUtils.copyByClass(payEntityOld, PayDTO.class);
            this.deleteWxPayCache(payDTOOld);
            // 更新到缓存
            Pay payEntityLocal = this.getById(payEntity.getId());
            PayDTO payDTO = BeanCopierUtils.copyByClass(payEntityLocal, PayDTO.class);
            this.addOrUpdateWxPayCache(payDTO);
        }
        return result;
    }

    /**
    * 删除微信支付
    * @param payId
    * @return
    */
    @Override
    public boolean deletePay(Long payId) {
        // 从缓存删除
        Pay payEntity = this.getById(payId);
        PayDTO payDTO = BeanCopierUtils.copyByClass(payEntity, PayDTO.class);
        this.deleteWxPayCache(payDTO);
        // 从数据库中删除
        boolean result = this.removeById(payId);
        return result;
    }
    /**
    * 批量删除微信支付
    * @param payIds
    * @return
    */
    @Override
    public boolean batchDeletePay(List<Long> payIds) {
        // 从缓存删除
        List<Pay> payEntityList = this.listByIds(payIds);
        if (!CollectionUtils.isEmpty(payEntityList))
        {
            for(Pay payDelete: payEntityList)
            {
                PayDTO payDTO = BeanCopierUtils.copyByClass(payDelete, PayDTO.class);
                this.deleteWxPayCache(payDTO);
            }
        }
        // 从数据库中删除
        boolean result = this.removeByIds(payIds);
        return result;
    }
    /**
    * 导出微信支付列表
    * @param queryPayDTO
    * @return
    */
    @Override
    public List<PayExportBO> exportPayList(QueryPayDTO queryPayDTO) {
        List<PayExportBO> payExportList = new ArrayList<>();
        List<PayDTO> payInfoList = this.queryPayList(queryPayDTO);
        if (!CollectionUtils.isEmpty(payInfoList))
        {
            for (PayDTO payInfo : payInfoList) {
                PayExportBO payExportBO = BeanCopierUtils.copyByClass(payInfo, PayExportBO.class);
                payExportList.add(payExportBO);
            }
        }
        return payExportList;
    }
    /**
    * 导入微信支付列表
    * @param file
    * @return
    */
    @Override
    public boolean importPayList(MultipartFile file) {
        boolean importSuccess = false;
        try {
            List<PayImportBO> payImportList = EasyExcel.read(file.getInputStream(), PayImportBO.class, null).sheet().doReadSync();
            if (!CollectionUtils.isEmpty(payImportList))
            {
                List<Pay> payList = new ArrayList<>();
                payImportList.stream().forEach(payImportBO-> {
                    payList.add(BeanCopierUtils.copyByClass(payImportBO, Pay.class));
                });
                importSuccess = this.saveBatch(payList);
            }
        } catch (IOException e) {
            log.error("批量导入微信支付数据时发生错误:{}", e);
            throw new BusinessException("批量导入失败:" + e);
        }
        return importSuccess;
    }

    /**
     * 初始化微信支付配置表列表
     * @return
     */
    @Override
    public void initWxPayList() {
        QueryPayDTO payDTO = new QueryPayDTO();
        payDTO.setStatus(String.valueOf(GitEggConstant.ENABLE));
        // 这里初始化所有的配置，不再只初始化已启用的配置
        List<PayDTO> payInfoList = payMapper.initWxPayList(payDTO);

        // 判断是否开启了租户模式，如果开启了，那么需要按租户进行分类存储
        if (enable) {
            Map<String, List<PayDTO>> payListMap =
                    payInfoList.stream().collect(Collectors.groupingBy(PayDTO::getAppId));
            payListMap.forEach((key, value) -> {
                String redisKey = WeChatPayConstant.WX_PAY_TENANT_CONFIG_KEY + key;
                redisTemplate.delete(redisKey);
                addWxPay(redisKey, value);
            });
        } else {
            redisTemplate.delete(WeChatPayConstant.WX_PAY_CONFIG_KEY);
            addWxPay(WeChatPayConstant.WX_PAY_CONFIG_KEY, payInfoList);
        }
    }

    /**
     * 通过appid获取租户id，忽略租户插件
     * @param payDTO
     * @return
     */
    @Override
    public PayDTO getWxPay(QueryPayDTO payDTO) {
        return payMapper.getWxPay(payDTO);
    }

    /**
     * 通过appid获取appid，忽略租户插件
     * @param appId
     * @return
     */
    @Override
    public String getWxPayAppId(String appId) {
        if (enable) {
            // 如果前端传了租户，那么先使用前端的租户，如果没有传租户，那么从系统中查询租户
            String tenantId = GitEggAuthUtils.getTenantId();
            if (!StringUtils.isEmpty(tenantId))
            {
                String payStr = (String) redisTemplate.opsForHash().get(WeChatPayConstant.WX_PAY_TENANT_CONFIG_KEY + appId, tenantId);
                if (!StringUtils.isEmpty(payStr))
                {
                    // 转为系统配置对象
                    try {
                        // 从缓存获取配置对象，如果md5配置和系统配置不一样，那么需要重新add
                        PayDTO payDTO = JsonUtils.jsonToPojo(payStr, PayDTO.class);
                        return this.ifConfig(payDTO);
                    } catch (Exception e) {
                        log.error("获取微信支付配置时发生异常：{}", e);
                        throw new BusinessException("获取微信支付配置时发生异常。");
                    }
                }
                // 缓存配置中没有也需要直接返回，因为有可能是配置文件配置的
                return tenantId + StrPool.UNDERLINE + appId;
            } else {
                String redisKey = WeChatPayConstant.WX_PAY_TENANT_CONFIG_KEY + appId;
                // 取缓存中所有appid的配置租户，如果存在多个租户，那么提示错误，让前端选择租户；如果只有一个租户，那么返回
                List<Object> values = redisTemplate.opsForHash().values(redisKey);
                if (!CollectionUtils.isEmpty(values))
                {
                    if (values.size() > GitEggConstant.Number.ONE)
                    {
                        throw new BusinessException("此微信支付配置在多个租户下，请选择所需要操作的租户。");
                    }

                    String payConfig = (String) values.stream().findFirst().orElse(null);
                    try {
                        PayDTO payConfigDTO = JsonUtils.jsonToPojo(payConfig, PayDTO.class);
                        return this.ifConfig(payConfigDTO);
                    } catch (Exception e) {
                        log.error("获取缓存微信支付配置失败:{}", e);
                        throw new BusinessException("微信支付已被禁用，请联系管理员");
                    }
                }
                else
                {
                    return AuthConstant.DEFAULT_TENANT_ID + StrPool.UNDERLINE + appId;
                }

            }
        } else {
            return AuthConstant.DEFAULT_TENANT_ID + StrPool.UNDERLINE + appId;
        }
    }

    /**
     * 根据AppId切换当前ThreadLocal的微信支付
     * @param appId
     * @return
     */
    @Override
    public boolean switchover(String appId)
    {
        String payAppId = this.getWxPayAppId(appId);
        return wxPayService.switchover(payAppId);
    }

    private String ifConfig(PayDTO payDTO)
    {
        if (null != payDTO && String.valueOf(GitEggConstant.DISABLE).equals(payDTO.getStatus()))
        {
            throw new BusinessException("微信支付已被禁用，请联系管理员");
        }

        if (wxPayService.switchover(payDTO.getTenantId() + StrPool.UNDERLINE + payDTO.getAppId()))
        {
            GitEggWxPayConfig config = (GitEggWxPayConfig)wxPayService.getConfig();
            String md5Local = config.getMd5();
            String md5Config = payDTO.getMd5();
            // 如果没有md5值，那么是系统配置文件配置的，不需要处理
            if (!StringUtils.isEmpty(md5Local) && !md5Local.equals(md5Config))
            {
                this.addConfig(payDTO);
            }
        }
        else
        {
            // 系统中不存在，则重新add
            this.addConfig(payDTO);
        }
        return payDTO.getTenantId() + StrPool.UNDERLINE + payDTO.getAppId();
    }

    private void addWxPay(String key, List<PayDTO> payList) {
        Map<String, String> payMap = new TreeMap<>();
        Optional.ofNullable(payList).orElse(new ArrayList<>()).forEach(config -> {
            try {
                payMap.put(config.getTenantId().toString(), JsonUtils.objToJson(config));
                redisTemplate.opsForHash().putAll(key, payMap);
                // wxPayService增加config
                this.addConfig(config);
            } catch (Exception e) {
                log.error("初始化微信支付配置失败：{}" , e);
            }
        });
    }

    private void addOrUpdateWxPayCache(PayDTO payDTO) {
        try {

            String redisKey = WeChatPayConstant.WX_PAY_CONFIG_KEY;
            if (enable) {
                redisKey = WeChatPayConstant.WX_PAY_TENANT_CONFIG_KEY + payDTO.getAppId();
            }
            redisTemplate.opsForHash().put(redisKey, payDTO.getTenantId().toString(), JsonUtils.objToJson(payDTO));

            // wxPayService增加config
            this.addConfig(payDTO);

        } catch (Exception e) {
            log.error("初始化微信支付配置失败：{}" , e);
        }
    }

    private void deleteWxPayCache(PayDTO payDTO) {
        try {

            String redisKey = WeChatPayConstant.WX_PAY_CONFIG_KEY;
            if (enable) {
                redisKey = WeChatPayConstant.WX_PAY_TENANT_CONFIG_KEY + payDTO.getAppId();
            }
            redisTemplate.opsForHash().delete(redisKey, payDTO.getTenantId().toString(), JsonUtils.objToJson(payDTO));
            // wxPayService删除config
            this.removeConfig(payDTO);
        } catch (Exception e) {
            log.error("初始化微信支付配置失败：{}" , e);
        }
    }

    private void addConfig(PayDTO payDTO){
        // wxPayService增加config
        GitEggWxPayConfig payConfig = new GitEggWxPayConfig();

        payConfig.setTenantId(null != payDTO.getTenantId() ? payDTO.getTenantId().toString() : AuthConstant.DEFAULT_TENANT_ID.toString());
        payConfig.setConfigKey(payConfig.getTenantId() + StrPool.UNDERLINE + payDTO.getAppId());

        payConfig.setAppId(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getAppId()));
        payConfig.setConfigKey(payConfig.getTenantId() + StrPool.UNDERLINE + payConfig.getAppId());
        payConfig.setMchId(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getMchId()));
        payConfig.setMchKey(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getMchKey()));
        payConfig.setSubAppId(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getSubAppId()));
        payConfig.setSubMchId(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getSubMchId()));
        payConfig.setKeyPath(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getKeyPath()));
        //以下是apiv3以及支付分相关
        payConfig.setServiceId(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getServiceId()));
        payConfig.setPayScoreNotifyUrl(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getPayScoreNotifyUrl()));
        payConfig.setPrivateKeyPath(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getPrivateKeyPath()));
        payConfig.setPrivateCertPath(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getPrivateCertPath()));
        payConfig.setCertSerialNo(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getCertSerialNo()));
        payConfig.setApiV3Key(org.apache.commons.lang3.StringUtils.trimToNull(payDTO.getApiV3Key()));

        wxPayService.addConfig(payConfig.getConfigKey(), payConfig);
    }

    private void removeConfig(PayDTO payDTO){
        // wxPayService删除config
        wxPayService.removeConfig((null != payDTO.getTenantId() ? payDTO.getTenantId().toString() : AuthConstant.DEFAULT_TENANT_ID.toString()) + StrPool.UNDERLINE + payDTO.getAppId());
    }
}
