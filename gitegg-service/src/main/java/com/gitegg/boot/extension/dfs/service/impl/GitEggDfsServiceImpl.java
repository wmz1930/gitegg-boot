package com.gitegg.boot.extension.dfs.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import com.gitegg.boot.extension.dfs.dto.DfsDTO;
import com.gitegg.boot.extension.dfs.dto.QueryDfsDTO;
import com.gitegg.boot.extension.dfs.entity.DfsFile;
import com.gitegg.boot.extension.dfs.factory.DfsFactory;
import com.gitegg.boot.extension.dfs.service.IDfsFileService;
import com.gitegg.boot.extension.dfs.service.IDfsService;
import com.gitegg.boot.extension.dfs.service.IGitEggDfsService;
import com.qiniu.util.Etag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * 短信发送接口实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggDfsServiceImpl implements IGitEggDfsService {

    private final DfsFactory dfsFactory;

    private final IDfsService dfsService;

    private final IDfsFileService dfsFileService;

    @Override
    public String uploadToken(String dfsCode) {
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        queryDfsDTO.setDfsCode(dfsCode);
        DfsDTO dfsDTO = dfsService.queryDfs(queryDfsDTO);
        IDfsBaseService dfsBaseService = dfsFactory.getDfsBaseService(dfsDTO);
        String token = dfsBaseService.uploadToken(dfsDTO.getBucket());
        return token;
    }
    
    @Override
    public GitEggDfsFile uploadFile(String dfsCode, MultipartFile file) {
        GitEggDfsFile gitEggDfsFile = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            gitEggDfsFile = this.uploadFile(dfsCode, bytes, file.getOriginalFilename(), file.getSize());
        } catch (IOException e) {
            throw new BusinessException("文件上传系统异常！");
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new BusinessException("文件上传系统异常！");
            }
        }
        return gitEggDfsFile;
    }
    
    @Override
    public GitEggDfsFile uploadFile(String dfsCode, byte[] bytes, String originalName, Long fileSize) {
        
        if (StringUtils.isEmpty(originalName))
        {
            throw new BusinessException("原文件名originalName不能为空");
        }
        
        if (null == fileSize)
        {
            fileSize = Long.valueOf(bytes.length);
        }
        
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;
        
        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }
        
        GitEggDfsFile gitEggDfsFile = null;
        DfsFile dfsFile = new DfsFile();
        ByteArrayInputStream hashStream = new ByteArrayInputStream(bytes);
        ByteArrayInputStream fileStream = new ByteArrayInputStream(bytes);
        try {

            dfsDTO = dfsService.queryDfs(queryDfsDTO);
            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);
  
            //获取文件后缀
            String extension = FileNameUtil.extName(originalName);
            String hash = Etag.stream(hashStream, fileSize);
            String fileName = StringUtils.isEmpty(extension) ? hash : (hash + "." + extension);
            
            // 保存文件上传记录
            dfsFile.setDfsId(dfsDTO.getId());
            dfsFile.setOriginalName(originalName);
            dfsFile.setFileName(fileName);
            dfsFile.setFileExtension(extension);
            dfsFile.setFileSize(fileSize);
            dfsFile.setFileStatus(GitEggConstant.ENABLE);
            
            //执行文件上传操作
            gitEggDfsFile = dfsFileService.uploadFile(fileStream, fileName);
            
            if (gitEggDfsFile != null)
            {
                gitEggDfsFile.setFileName(originalName);
                gitEggDfsFile.setKey(hash);
                gitEggDfsFile.setHash(hash);
                gitEggDfsFile.setFileSize(fileSize);
            }
            
            dfsFile.setAccessUrl(gitEggDfsFile.getFileUrl());
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            dfsFile.setFileStatus(GitEggConstant.DISABLE);
            dfsFile.setComments(String.valueOf(e));
        } finally {
            try {
                if(hashStream != null){
                    hashStream.close();
                }
                if(fileStream != null){
                    fileStream.close();
                }
            } catch (IOException e) {
                throw new BusinessException("文件上传系统异常！");
            }
            dfsFileService.save(dfsFile);
        }
        
        return gitEggDfsFile;
    }

    @Override
    public String getFileUrl(String dfsCode, String fileName) {
        String fileUrl = null;

        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;
        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }

        try {
            dfsDTO = dfsService.queryDfs(queryDfsDTO);
            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);
            fileUrl = dfsFileService.getFileUrl(fileName);
        }
        catch (Exception e)
        {
            log.error("文件上传失败：{}", e);
        }
        return fileUrl;
    }

    @Override
    public OutputStream downloadFile(String dfsCode, String fileName, OutputStream outputStream) {
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;
        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }

        try {
            dfsDTO = dfsService.queryDfs(queryDfsDTO);
            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);
            outputStream = dfsFileService.getFileObject(fileName, outputStream);
        }
        catch (Exception e)
        {
            log.error("文件上传失败：{}", e);
        }
        return outputStream;
    }
}
