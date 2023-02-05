package com.gitegg.boot.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.boot.base.entity.District;
import com.gitegg.boot.base.service.IDistrictService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/base/district")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DistrictController|地区配置表前端控制器", tags = {"地区配置"})
public class DistrictController {

    private final IDistrictService districtService;

    /**
     * 查询地区
     */
    @GetMapping("/list")
    @ApiOperation("查询地区信息")
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
