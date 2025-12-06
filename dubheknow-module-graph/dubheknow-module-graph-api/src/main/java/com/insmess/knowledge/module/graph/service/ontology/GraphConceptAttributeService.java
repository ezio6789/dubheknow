package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptAttributePO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeSaveReqVO;

/**
 * 概念属性Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphConceptAttributeService extends IService<GraphConceptAttributePO> {

    /**
     * 获得概念属性分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念属性分页列表
     */
    Page<GraphConceptAttributePO> pageGraphConceptAttribute(GraphConceptAttributePageReqVO pageReqVO);

    /**
     * 创建概念属性
     *
     * @param saveReqVO 概念属性信息
     * @return 概念属性编号
     */
    Long saveGraphConceptAttribute(GraphConceptAttributeSaveReqVO saveReqVO);

    /**
     * 更新概念属性
     *
     * @param updateReqVO 概念属性信息
     */
    int updateGraphConceptAttribute(GraphConceptAttributeSaveReqVO updateReqVO);

    /**
     * 删除概念属性
     *
     * @param idList 概念属性编号
     */
    int removeGraphConceptAttribute(Collection<Long> idList);

    /**
     * 获得概念属性详情
     *
     * @param id 概念属性编号
     * @return 概念属性
     */
    GraphConceptAttributePO getGraphConceptAttributeById(Long id);

    /**
     * 获得全部概念属性列表
     *
     * @return 概念属性列表
     */
    List<GraphConceptAttributePO> listGraphConceptAttribute();

    /**
     * 获得全部概念属性 Map
     *
     * @return 概念属性 Map
     */
    Map<Long, GraphConceptAttributePO> mapGraphConceptAttribute();


    /**
     * 导入概念属性数据
     *
     * @param importExcelList 概念属性数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphConceptAttribute(List<GraphConceptAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
