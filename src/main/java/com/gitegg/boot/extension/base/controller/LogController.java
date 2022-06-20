package com.gitegg.boot.extension.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.boot.extension.base.dto.LogDTO;
import com.gitegg.boot.extension.base.dto.QueryLogDTO;
import com.gitegg.boot.extension.base.service.ILogService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/extension/base/log")
public class LogController {

    private final ILogService logService;

    /**
     * 查询所有日志
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询操作日志列表")
    public PageResult<LogDTO> list(QueryLogDTO log, Page<LogDTO> page) {
        Page<LogDTO> pageLog = logService.selectLogList(page, log);
        PageResult<LogDTO> pageResult = new PageResult<>(pageLog.getTotal(), pageLog.getRecords());
        return pageResult;
    }
}
