package com.gitegg.boot.extension.mail.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.gitegg.boot.extension.mail.entity.MailChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.boot.extension.mail.dto.MailChannelDTO;
import com.gitegg.boot.extension.mail.dto.QueryMailChannelDTO;

import java.util.List;

/**
 * <p>
 * 邮件渠道表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
public interface MailChannelMapper extends BaseMapper<MailChannel> {

    /**
    * 分页查询邮件渠道表列表
    * @param page
    * @param mailChannelDTO
    * @return
    */
    Page<MailChannelDTO> queryMailChannelList(Page<MailChannelDTO> page, @Param("mailChannel") QueryMailChannelDTO mailChannelDTO);

    /**
    * 查询邮件渠道表列表
    * @param mailChannelDTO
    * @return
    */
    List<MailChannelDTO> queryMailChannelList(@Param("mailChannel") QueryMailChannelDTO mailChannelDTO);

    /**
    * 查询邮件渠道表信息
    * @param mailChannelDTO
    * @return
    */
    MailChannelDTO queryMailChannel(@Param("mailChannel") QueryMailChannelDTO mailChannelDTO);

    /**
     * 排除多租户插件查询邮件配置列表
     * @param mailChannelDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<MailChannelDTO> initMailChannelList(@Param("mailChannel") QueryMailChannelDTO mailChannelDTO);
}
