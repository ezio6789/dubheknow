package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationRespVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaRelationPO;
/**
 * 关系配置Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphSchemaRelationService extends IService<GraphSchemaRelationPO> {

    /**
     * 获得关系配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系配置分页列表
     */
    Page<GraphSchemaRelationPO> pageGraphSchemaRelation(GraphSchemaRelationPageReqVO pageReqVO);

    /**
     * 创建关系配置
     *
     * @param saveReqVO 关系配置信息
     * @return 关系配置编号
     */
    Long saveGraphSchemaRelation(GraphSchemaRelationSaveReqVO saveReqVO);

    /**
     * 更新关系配置
     *
     * @param updateReqVO 关系配置信息
     */
    int updateGraphSchemaRelation(GraphSchemaRelationSaveReqVO updateReqVO);

    /**
     * 删除关系配置
     *
     * @param idList 关系配置编号
     */
    int removeGraphSchemaRelation(Collection<Long> idList);

    /**
     * 获得关系配置详情
     *
     * @param id 关系配置编号
     * @return 关系配置
     */
    GraphSchemaRelationPO getGraphSchemaRelationById(Long id);

    /**
     * 获得全部关系配置列表
     *
     * @return 关系配置列表
     */
    List<GraphSchemaRelationPO> listGraphSchemaRelation();

    /**
     * 获得全部关系配置 Map
     *
     * @return 关系配置 Map
     */
    Map<Long, GraphSchemaRelationPO> mapGraphSchemaRelation();


    /**
     * 导入关系配置数据
     *
     * @param importExcelList 关系配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphSchemaRelation(List<GraphSchemaRelationRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
