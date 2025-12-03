package com.insmess.knowledge.module.knowbase.service.unstruct;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentSaveReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentRespVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentContentPO;
/**
 * 知识内容Service接口
 *
 * @author insmess
 * @date 2025-12-03
 */
public interface KnowbaseDocumentContentService extends IService<KnowbaseDocumentContentPO> {

    /**
     * 获得知识内容分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识内容分页列表
     */
    Page<KnowbaseDocumentContentPO> pageKnowbaseDocumentContent(KnowbaseDocumentContentPageReqVO pageReqVO);

    /**
     * 创建知识内容
     *
     * @param saveReqVO 知识内容信息
     * @return 知识内容编号
     */
    Long saveKnowbaseDocumentContent(KnowbaseDocumentContentSaveReqVO saveReqVO);

    /**
     * 更新知识内容
     *
     * @param updateReqVO 知识内容信息
     */
    int updateKnowbaseDocumentContent(KnowbaseDocumentContentSaveReqVO updateReqVO);

    /**
     * 删除知识内容
     *
     * @param idList 知识内容编号
     */
    int removeKnowbaseDocumentContent(Collection<Long> idList);

    /**
     * 获得知识内容详情
     *
     * @param id 知识内容编号
     * @return 知识内容
     */
    KnowbaseDocumentContentPO getKnowbaseDocumentContentById(Long id);

    /**
     * 获得全部知识内容列表
     *
     * @return 知识内容列表
     */
    List<KnowbaseDocumentContentPO> listKnowbaseDocumentContent();

    /**
     * 获得全部知识内容 Map
     *
     * @return 知识内容 Map
     */
    Map<Long, KnowbaseDocumentContentPO> mapKnowbaseDocumentContent();


    /**
     * 导入知识内容数据
     *
     * @param importExcelList 知识内容数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKnowbaseDocumentContent(List<KnowbaseDocumentContentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
