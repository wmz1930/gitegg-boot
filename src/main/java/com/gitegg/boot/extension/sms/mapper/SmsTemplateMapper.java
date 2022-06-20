package com.gitegg.boot.extension.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.sms.dto.QuerySmsTemplateDTO;
import com.gitegg.boot.extension.sms.dto.SmsTemplateDTO;
import com.gitegg.boot.extension.sms.entity.SmsTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 短信配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-24
 */
public interface SmsTemplateMapper extends BaseMapper<SmsTemplate> {

    /**
    * 分页查询短信配置表列表
    * @param page
    * @param smsTemplateDTO
    * @return
    */
    Page<SmsTemplateDTO> querySmsTemplateList(Page<SmsTemplateDTO> page, @Param("smsTemplate") QuerySmsTemplateDTO smsTemplateDTO);

    /**
    * 查询短信配置表列表
    * @param smsTemplateDTO
    * @return
    */
    List<SmsTemplateDTO> querySmsTemplateList(@Param("smsTemplate") QuerySmsTemplateDTO smsTemplateDTO);

    /**
    * 查询短信配置表信息
    * @param smsTemplateDTO
    * @return
    */
    SmsTemplateDTO querySmsTemplate(@Param("smsTemplate") QuerySmsTemplateDTO smsTemplateDTO);
}
