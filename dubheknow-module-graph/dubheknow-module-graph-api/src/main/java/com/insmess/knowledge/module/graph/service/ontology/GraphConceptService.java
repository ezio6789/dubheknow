package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptPageReqVO;

/**
 * 概念配置Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphConceptService extends IService<GraphConceptPO> {

    /**
     * 获得概念配置树
     *
     * @param extConcept 概念配置信息
     * @return 概念配置树
     */
    List<GraphConceptPO> listExtConceptTree(GraphConceptPageReqVO extConcept);

    /**
     * 获取所有的父级concept（仅获取父级）
     * @param id
     * @return
     */
    List<GraphConceptPO> getParentListByPid(Long id);

    /**
     * 获取概念图（本体）
     *
     * @return 概念配置图
     */
    Map<String, Object> getGraphOntology(Long knowledgeId);

    /**
     * 获得概念配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念配置分页列表
     */
    Page<GraphConceptPO> pageGraphConcept(GraphConceptPageReqVO pageReqVO);

    /**
     * 创建概念配置
     *
     * @param saveReqVO 概念配置信息
     * @return 概念配置编号
     */
    Long saveGraphConcept(GraphConceptSaveReqVO saveReqVO);

    /**
     * 更新概念配置
     *
     * @param updateReqVO 概念配置信息
     */
    int updateGraphConcept(GraphConceptSaveReqVO updateReqVO);

    /**
     * 删除概念配置
     *
     * @param idList 概念配置编号
     */
    int removeGraphConcept(Collection<Long> idList);

    /**
     * 获得概念配置详情
     *
     * @param id 概念配置编号
     * @return 概念配置
     */
    GraphConceptPO getGraphConceptById(Long id);

    /**
     * 获得全部概念配置列表
     *
     * @return 概念配置列表
     */
    List<GraphConceptPO> listGraphConcept();

    /**
     * 获得全部概念配置 Map
     *
     * @return 概念配置 Map
     */
    Map<Long, GraphConceptPO> mapGraphConcept();


    /**
     * 导入概念配置数据
     *
     * @param importExcelList 概念配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphConcept(List<GraphConceptRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
