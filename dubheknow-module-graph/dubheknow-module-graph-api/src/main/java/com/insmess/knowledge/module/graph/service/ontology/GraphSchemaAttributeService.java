package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeRespVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaAttributePO;
/**
 * 概念属性Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphSchemaAttributeService extends IService<GraphSchemaAttributePO> {

    /**
     * 获得概念属性分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念属性分页列表
     */
    Page<GraphSchemaAttributePO> pageGraphSchemaAttribute(GraphSchemaAttributePageReqVO pageReqVO);

    /**
     * 创建概念属性
     *
     * @param saveReqVO 概念属性信息
     * @return 概念属性编号
     */
    Long saveGraphSchemaAttribute(GraphSchemaAttributeSaveReqVO saveReqVO);

    /**
     * 更新概念属性
     *
     * @param updateReqVO 概念属性信息
     */
    int updateGraphSchemaAttribute(GraphSchemaAttributeSaveReqVO updateReqVO);

    /**
     * 删除概念属性
     *
     * @param idList 概念属性编号
     */
    int removeGraphSchemaAttribute(Collection<Long> idList);

    /**
     * 获得概念属性详情
     *
     * @param id 概念属性编号
     * @return 概念属性
     */
    GraphSchemaAttributePO getGraphSchemaAttributeById(Long id);

    /**
     * 获得全部概念属性列表
     *
     * @return 概念属性列表
     */
    List<GraphSchemaAttributePO> listGraphSchemaAttribute();

    /**
     * 获得全部概念属性 Map
     *
     * @return 概念属性 Map
     */
    Map<Long, GraphSchemaAttributePO> mapGraphSchemaAttribute();


    /**
     * 导入概念属性数据
     *
     * @param importExcelList 概念属性数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphSchemaAttribute(List<GraphSchemaAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
