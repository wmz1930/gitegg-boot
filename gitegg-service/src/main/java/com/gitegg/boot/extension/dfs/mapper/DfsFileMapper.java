package com.gitegg.boot.extension.dfs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.dfs.dto.DfsFileDTO;
import com.gitegg.boot.extension.dfs.dto.QueryDfsFileDTO;
import com.gitegg.boot.extension.dfs.entity.DfsFile;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 分布式存储文件记录表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-08
 */
public interface DfsFileMapper extends BaseMapper<DfsFile> {

    /**
    * 查询分布式存储文件记录表列表
    * @param page
    * @param dfsFileDTO
    * @return
    */
    Page<DfsFileDTO> queryDfsFileList(Page<DfsFileDTO> page, @Param("dfsFile") QueryDfsFileDTO dfsFileDTO);

    /**
    * 查询分布式存储文件记录表信息
    * @param dfsFileDTO
    * @return
    */
    DfsFileDTO queryDfsFile(@Param("dfsFile") QueryDfsFileDTO dfsFileDTO);
}
