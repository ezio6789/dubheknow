package com.insmess.knowledge.module.knowbase.service.unstruct;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentSaveReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentRespVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentPO;
/**
 * 知识文件Service接口
 *
 * @author insmess
 * @date 2025-12-03
 */
public interface KnowbaseDocumentService extends IService<KnowbaseDocumentPO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    Page<KnowbaseDocumentPO> pageKnowbaseDocument(KnowbaseDocumentPageReqVO pageReqVO);

    /**
     * 创建知识文件
     *
     * @param saveReqVO 知识文件信息
     * @return 知识文件编号
     */
    Long saveKnowbaseDocument(KnowbaseDocumentSaveReqVO saveReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKnowbaseDocument(KnowbaseDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKnowbaseDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KnowbaseDocumentPO getKnowbaseDocumentById(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KnowbaseDocumentPO> listKnowbaseDocument();

    /**
     * 获得全部知识文件 Map
     *
     * @return 知识文件 Map
     */
    Map<Long, KnowbaseDocumentPO> mapKnowbaseDocument();


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKnowbaseDocument(List<KnowbaseDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
