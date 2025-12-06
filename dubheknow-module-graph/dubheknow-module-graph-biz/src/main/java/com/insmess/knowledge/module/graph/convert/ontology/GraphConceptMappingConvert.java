package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingSaveReqVO;

/**
 * 概念映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphConceptMappingConvert {
    GraphConceptMappingConvert INSTANCE = Mappers.getMapper(GraphConceptMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphConceptMappingPageReqVO 请求参数
     * @return GraphConceptMappingPO
     */
     GraphConceptMappingPO convertToPO(GraphConceptMappingPageReqVO graphConceptMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphConceptMappingSaveReqVO 保存请求参数
     * @return GraphConceptMappingPO
     */
     GraphConceptMappingPO convertToPO(GraphConceptMappingSaveReqVO graphConceptMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphConceptMappingPO 实体对象
     * @return GraphConceptMappingRespVO
     */
     GraphConceptMappingRespVO convertToRespVO(GraphConceptMappingPO graphConceptMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphConceptMappingPOList 实体对象列表
     * @return List<GraphConceptMappingRespVO>
     */
     List<GraphConceptMappingRespVO> convertToRespVOList(List<GraphConceptMappingPO> graphConceptMappingPOList);
}
