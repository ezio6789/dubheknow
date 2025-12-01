package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphRelationMappingPO;

/**
 * 关系映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphRelationMappingConvert {
    GraphRelationMappingConvert INSTANCE = Mappers.getMapper(GraphRelationMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphRelationMappingPageReqVO 请求参数
     * @return GraphRelationMappingPO
     */
     GraphRelationMappingPO convertToPO(GraphRelationMappingPageReqVO graphRelationMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphRelationMappingSaveReqVO 保存请求参数
     * @return GraphRelationMappingPO
     */
     GraphRelationMappingPO convertToPO(GraphRelationMappingSaveReqVO graphRelationMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphRelationMappingPO 实体对象
     * @return GraphRelationMappingRespVO
     */
     GraphRelationMappingRespVO convertToRespVO(GraphRelationMappingPO graphRelationMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphRelationMappingPOList 实体对象列表
     * @return List<GraphRelationMappingRespVO>
     */
     List<GraphRelationMappingRespVO> convertToRespVOList(List<GraphRelationMappingPO> graphRelationMappingPOList);
}
