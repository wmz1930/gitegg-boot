package com.gitegg.boot.extension.dfs.service;

import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * 业务文件上传下载接口实现
 *
 */
public interface IGitEggDfsService {


    /**
     * 获取文件上传的 token
     * @param dfsCode
     * @return
     */
    String uploadToken(String dfsCode);


    /**
     * 上传文件(前端)
     *
     * @param dfsCode
     * @param file
     * @return
     */
    GitEggDfsFile uploadFile(String dfsCode, MultipartFile file);
    
    /**
     * 上传文件(接口)
     * @param dfsCode
     * @param bytes
     * @param originalName （必传）
     * @param fileSize
     * @return
     */
    GitEggDfsFile uploadFile(String dfsCode, byte[] bytes, String originalName, Long fileSize);
    
    /**
     * 获取文件访问链接
     * @param dfsCode
     * @param fileName
     * @return
     */
    String getFileUrl(String dfsCode, String fileName);


    /**
     * 下载文件
     * @param dfsCode
     * @param fileName
     * @return
     */
    OutputStream downloadFile(String dfsCode, String fileName, OutputStream outputStream);
}
