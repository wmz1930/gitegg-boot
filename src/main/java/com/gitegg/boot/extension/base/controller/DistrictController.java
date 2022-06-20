package com.gitegg.boot.extension.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.result.Result;
import com.gitegg.boot.extension.base.entity.District;
import com.gitegg.boot.extension.base.service.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 地区字典
 * </p>
 *
 * @author GitEgg
 * @since 2018-05-26
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RefreshScope
@RequestMapping("/extension/base/district")
public class DistrictController {

    private final IDistrictService districtService;

    /**
     * 查询地区
     */
    @GetMapping("/list")
    public Result<List<District>> list(String code) {
        Integer parentId = 0;
        if (!StringUtils.isEmpty(code)) {
            QueryWrapper<District> ew = new QueryWrapper<>();
            ew.eq("code", code);
            District district = districtService.getOne(ew);
            if (null != district) {
                parentId = district.getId();
            }
        }
        QueryWrapper<District> ewr = new QueryWrapper<>();
        ewr.eq("parent_id", parentId);
        List<District> districtList = districtService.list(ewr);
        return Result.data(districtList);
    }
}
