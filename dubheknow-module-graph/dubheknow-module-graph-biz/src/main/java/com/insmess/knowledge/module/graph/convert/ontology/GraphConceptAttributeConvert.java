package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;

import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributePageReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptAttributePO;

/**
 * 概念属性 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphConceptAttributeConvert {
    GraphConceptAttributeConvert INSTANCE = Mappers.getMapper(GraphConceptAttributeConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphConceptAttributePageReqVO 请求参数
     * @return GraphConceptAttributePO
     */
     GraphConceptAttributePO convertToPO(GraphConceptAttributePageReqVO graphConceptAttributePageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphConceptAttributeSaveReqVO 保存请求参数
     * @return GraphConceptAttributePO
     */
     GraphConceptAttributePO convertToPO(GraphConceptAttributeSaveReqVO graphConceptAttributeSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphConceptAttributePO 实体对象
     * @return GraphConceptAttributeRespVO
     */
     GraphConceptAttributeRespVO convertToRespVO(GraphConceptAttributePO graphConceptAttributePO);

    /**
     * POList 转换为 RespVOList
     * @param graphConceptAttributePOList 实体对象列表
     * @return List<GraphConceptAttributeRespVO>
     */
     List<GraphConceptAttributeRespVO> convertToRespVOList(List<GraphConceptAttributePO> graphConceptAttributePOList);
}
