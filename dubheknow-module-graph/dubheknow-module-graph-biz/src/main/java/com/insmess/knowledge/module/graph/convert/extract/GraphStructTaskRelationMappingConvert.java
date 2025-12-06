package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskRelationMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingSaveReqVO;

/**
 * 关系映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphStructTaskRelationMappingConvert {
    GraphStructTaskRelationMappingConvert INSTANCE = Mappers.getMapper(GraphStructTaskRelationMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphStructTaskRelationMappingPageReqVO 请求参数
     * @return GraphStructTaskRelationMappingPO
     */
     GraphStructTaskRelationMappingPO convertToPO(GraphStructTaskRelationMappingPageReqVO graphStructTaskRelationMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphStructTaskRelationMappingSaveReqVO 保存请求参数
     * @return GraphStructTaskRelationMappingPO
     */
     GraphStructTaskRelationMappingPO convertToPO(GraphStructTaskRelationMappingSaveReqVO graphStructTaskRelationMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphStructTaskRelationMappingPO 实体对象
     * @return GraphStructTaskRelationMappingRespVO
     */
     GraphStructTaskRelationMappingRespVO convertToRespVO(GraphStructTaskRelationMappingPO graphStructTaskRelationMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphStructTaskRelationMappingPOList 实体对象列表
     * @return List<GraphStructTaskRelationMappingRespVO>
     */
     List<GraphStructTaskRelationMappingRespVO> convertToRespVOList(List<GraphStructTaskRelationMappingPO> graphStructTaskRelationMappingPOList);
}
