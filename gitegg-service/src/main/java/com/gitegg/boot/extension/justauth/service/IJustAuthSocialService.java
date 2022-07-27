package com.gitegg.boot.extension.justauth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.boot.extension.justauth.dto.CreateJustAuthSocialDTO;
import com.gitegg.boot.extension.justauth.dto.JustAuthSocialDTO;
import com.gitegg.boot.extension.justauth.dto.QueryJustAuthSocialDTO;
import com.gitegg.boot.extension.justauth.dto.UpdateJustAuthSocialDTO;
import com.gitegg.boot.extension.justauth.entity.JustAuthSocial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 第三方用户信息 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-23
 */
public interface IJustAuthSocialService extends IService<JustAuthSocial> {

    /**
    * 分页查询第三方用户信息列表
    * @param page
    * @param queryJustAuthSocialDTO
    * @return
    */
    Page<JustAuthSocialDTO> queryJustAuthSocialList(Page<JustAuthSocialDTO> page, QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
    * 查询第三方用户信息列表
    * @param queryJustAuthSocialDTO
    * @return
    */
    List<JustAuthSocialDTO> queryJustAuthSocialList(QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
    * 查询第三方用户信息详情
    * @param queryJustAuthSocialDTO
    * @return
    */
    JustAuthSocialDTO queryJustAuthSocial(QueryJustAuthSocialDTO queryJustAuthSocialDTO);
    
    /**
     * 查询第三方用户绑定的系统用户id
     * @param justAuthSocialDTO
     * @return
     */
    Long queryUserIdBySocial(@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

    /**
    * 创建第三方用户信息
    * @param justAuthSocial
    * @return
    */
    JustAuthSocial createJustAuthSocial(CreateJustAuthSocialDTO justAuthSocial);

    /**
    * 更新第三方用户信息
    * @param justAuthSocial
    * @return
    */
    boolean updateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial);
    
    /**
     * 创建或第三方用户信息
     * @param justAuthSocial
     * @return
     */
    JustAuthSocial createOrUpdateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial);
    
    /**
    * 删除第三方用户信息
    * @param justAuthSocialId
    * @return
    */
    boolean deleteJustAuthSocial(Long justAuthSocialId);

    /**
    * 批量删除第三方用户信息
    * @param justAuthSocialIds
    * @return
    */
    boolean batchDeleteJustAuthSocial(List<Long> justAuthSocialIds);
}
