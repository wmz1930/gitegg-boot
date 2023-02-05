package com.gitegg.boot.extension.dfs.controller;

import cn.hutool.core.util.ArrayUtil;
import com.gitegg.boot.extension.dfs.dto.QueryDfsFileDTO;
import com.gitegg.boot.extension.dfs.service.IGitEggDfsService;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分布式存储上传 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
@RestController
@RequestMapping("/extension")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "GitEggDfsController|文件上传前端控制器", tags = {"文件上传"})
public class GitEggDfsController {

    private final IGitEggDfsService gitEggDfsService;

    /**
     * 上传文件
     * @param uploadFile
     * @param dfsCode
     * @return
     */
    @PostMapping("/upload/file")
    @ApiOperation("上传文件")
    public Result<?> uploadFile(@RequestParam("uploadFile") MultipartFile[] uploadFile, String dfsCode) {
        List<GitEggDfsFile> gitEggDfsFiles = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(uploadFile))
        {
            for (MultipartFile file : uploadFile) {
                GitEggDfsFile gitEggDfsFile = gitEggDfsService.uploadFile(dfsCode, file);
                // 查询文件访问链接
                String fileUrl = gitEggDfsService.getFileUrl(dfsCode, gitEggDfsFile.getEncodedFileName());
                gitEggDfsFile.setFileUrl(fileUrl);
                gitEggDfsFiles.add(gitEggDfsFile);
            }
        }
        else
        {
            throw new BusinessException("没有选择上传文件");
        }
        return Result.data(gitEggDfsFiles);
    }


    /**
     * 通过文件名获取文件访问链接
     */
    @GetMapping("/get/file/url")
    @ApiOperation(value = "查询分布式存储配置表详情")
    public Result<?> query(String dfsCode, String fileName) {
        String fileUrl = gitEggDfsService.getFileUrl(dfsCode, fileName);
        return Result.data(fileUrl);
    }

    /**
     * 通过批量文件名获取文件访问链接
     */
    @PostMapping("/batch/get/file/url")
    @ApiOperation(value = "查询分布式存储配置表详情")
    public Result<?> queryBatch( @RequestBody QueryDfsFileDTO fileQuery) {
        List<GitEggDfsFile> gitEggDfsFiles = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(fileQuery.getFileNames()))
        {
            for (String fileName : fileQuery.getFileNames()) {
                String fileUrl = gitEggDfsService.getFileUrl(fileQuery.getDfsCode(), fileName);
                GitEggDfsFile gitEggDfsFile = new GitEggDfsFile();
                gitEggDfsFile.setFileName(fileName);
                gitEggDfsFile.setFileUrl(fileUrl);
                gitEggDfsFiles.add(gitEggDfsFile);
            }
        }
        return Result.data(gitEggDfsFiles);
    }

    /**
     * 通过文件名以文件流的方式下载文件
     */
    @GetMapping("/get/file/download")
    @ApiOperation("文件下载")
    public void downloadFile(HttpServletResponse response,HttpServletRequest request,String dfsCode, String fileName) {
        if (fileName != null) {
            response.setCharacterEncoding(request.getCharacterEncoding());
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os = gitEggDfsService.downloadFile(dfsCode, fileName, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
