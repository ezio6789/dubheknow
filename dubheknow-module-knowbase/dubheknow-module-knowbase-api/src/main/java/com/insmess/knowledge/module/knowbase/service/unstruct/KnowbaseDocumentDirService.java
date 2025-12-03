package com.insmess.knowledge.module.knowbase.service.unstruct;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirSaveReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirRespVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentDirPO;
/**
 * 知识目录Service接口
 *
 * @author insmess
 * @date 2025-12-03
 */
public interface KnowbaseDocumentDirService extends IService<KnowbaseDocumentDirPO> {

    /**
     * 获得知识目录分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识目录分页列表
     */
    Page<KnowbaseDocumentDirPO> pageKnowbaseDocumentDir(KnowbaseDocumentDirPageReqVO pageReqVO);

    /**
     * 创建知识目录
     *
     * @param saveReqVO 知识目录信息
     * @return 知识目录编号
     */
    Long saveKnowbaseDocumentDir(KnowbaseDocumentDirSaveReqVO saveReqVO);

    /**
     * 更新知识目录
     *
     * @param updateReqVO 知识目录信息
     */
    int updateKnowbaseDocumentDir(KnowbaseDocumentDirSaveReqVO updateReqVO);

    /**
     * 删除知识目录
     *
     * @param idList 知识目录编号
     */
    int removeKnowbaseDocumentDir(Collection<Long> idList);

    /**
     * 获得知识目录详情
     *
     * @param id 知识目录编号
     * @return 知识目录
     */
    KnowbaseDocumentDirPO getKnowbaseDocumentDirById(Long id);

    /**
     * 获得全部知识目录列表
     *
     * @return 知识目录列表
     */
    List<KnowbaseDocumentDirPO> listKnowbaseDocumentDir();

    /**
     * 获得全部知识目录 Map
     *
     * @return 知识目录 Map
     */
    Map<Long, KnowbaseDocumentDirPO> mapKnowbaseDocumentDir();


    /**
     * 导入知识目录数据
     *
     * @param importExcelList 知识目录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKnowbaseDocumentDir(List<KnowbaseDocumentDirRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
