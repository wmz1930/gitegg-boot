package com.gitegg.boot.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.base.dto.LogDTO;
import com.gitegg.boot.base.dto.QueryLogDTO;
import com.gitegg.boot.base.entity.Log;
import com.gitegg.boot.base.mapper.LogMapper;
import com.gitegg.boot.base.service.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private final LogMapper logMapper;

    @Override
    public Page<LogDTO> selectLogList(Page<LogDTO> page, QueryLogDTO log) {
        Page<LogDTO> pageLogInfo = logMapper.selectLogList(page, log);
        return pageLogInfo;
    }
}
