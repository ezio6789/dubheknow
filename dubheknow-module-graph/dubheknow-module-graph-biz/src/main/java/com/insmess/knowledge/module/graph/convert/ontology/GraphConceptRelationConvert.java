package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptRelationPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 关系配置 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphConceptRelationConvert {
    GraphConceptRelationConvert INSTANCE = Mappers.getMapper(GraphConceptRelationConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphConceptRelationPageReqVO 请求参数
     * @return GraphConceptRelationPO
     */
     GraphConceptRelationPO convertToPO(GraphConceptRelationPageReqVO graphConceptRelationPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphConceptRelationSaveReqVO 保存请求参数
     * @return GraphConceptRelationPO
     */
     GraphConceptRelationPO convertToPO(GraphConceptRelationSaveReqVO graphConceptRelationSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphConceptRelationPO 实体对象
     * @return GraphConceptRelationRespVO
     */
     GraphConceptRelationRespVO convertToRespVO(GraphConceptRelationPO graphConceptRelationPO);

    /**
     * POList 转换为 RespVOList
     * @param graphConceptRelationPOList 实体对象列表
     * @return List<GraphConceptRelationRespVO>
     */
     List<GraphConceptRelationRespVO> convertToRespVOList(List<GraphConceptRelationPO> graphConceptRelationPOList);
}
