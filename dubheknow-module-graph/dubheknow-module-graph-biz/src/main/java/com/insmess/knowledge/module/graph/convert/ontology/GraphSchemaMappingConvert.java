package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaMappingPO;

/**
 * 概念映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphSchemaMappingConvert {
    GraphSchemaMappingConvert INSTANCE = Mappers.getMapper(GraphSchemaMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphSchemaMappingPageReqVO 请求参数
     * @return GraphSchemaMappingPO
     */
     GraphSchemaMappingPO convertToPO(GraphSchemaMappingPageReqVO graphSchemaMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphSchemaMappingSaveReqVO 保存请求参数
     * @return GraphSchemaMappingPO
     */
     GraphSchemaMappingPO convertToPO(GraphSchemaMappingSaveReqVO graphSchemaMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphSchemaMappingPO 实体对象
     * @return GraphSchemaMappingRespVO
     */
     GraphSchemaMappingRespVO convertToRespVO(GraphSchemaMappingPO graphSchemaMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphSchemaMappingPOList 实体对象列表
     * @return List<GraphSchemaMappingRespVO>
     */
     List<GraphSchemaMappingRespVO> convertToRespVOList(List<GraphSchemaMappingPO> graphSchemaMappingPOList);
}
