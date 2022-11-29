package com.gitegg.boot.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.base.entity.District;
import com.gitegg.boot.base.mapper.DistrictMapper;
import com.gitegg.boot.base.service.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2018-05-26
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements IDistrictService {
    
    @Override
    public District queryDistrict(String districtCode) {
        return null;
    }
}
