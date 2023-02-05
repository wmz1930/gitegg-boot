package com.gitegg.boot.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.base.dto.LogDTO;
import com.gitegg.boot.base.dto.QueryLogDTO;
import com.gitegg.boot.base.service.ILogService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
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
@RequestMapping("/base/log")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "LogController|日志查询前端控制器", tags = {"日志查询"})
public class LogController {

    private final ILogService logService;

    /**
     * 查询所有日志
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询操作日志列表")
    public Result<Page<LogDTO>> list(QueryLogDTO log, Page<LogDTO> page) {
        Page<LogDTO> pageLog = logService.selectLogList(page, log);
        return Result.data(pageLog);
    }
}
