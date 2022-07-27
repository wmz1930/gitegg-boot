package com.gitegg.boot.extension.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.base.dto.LogDTO;
import com.gitegg.boot.extension.base.dto.QueryLogDTO;
import com.gitegg.boot.extension.base.entity.Log;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
public interface LogMapper extends BaseMapper<Log> {
    /**
     * 分页查询操作日志
     * 
     * @param page
     * @param log
     * @return logInfo
     */
    Page<LogDTO> selectLogList(Page<LogDTO> page, @Param("log") QueryLogDTO log);
}
