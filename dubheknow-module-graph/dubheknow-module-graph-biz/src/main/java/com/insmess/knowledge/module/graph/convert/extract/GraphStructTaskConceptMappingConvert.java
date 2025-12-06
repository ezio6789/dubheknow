package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingSaveReqVO;

/**
 * 概念映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphStructTaskConceptMappingConvert {
    GraphStructTaskConceptMappingConvert INSTANCE = Mappers.getMapper(GraphStructTaskConceptMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphStructTaskConceptMappingPageReqVO 请求参数
     * @return GraphStructTaskConceptMappingPO
     */
     GraphStructTaskConceptMappingPO convertToPO(GraphStructTaskConceptMappingPageReqVO graphStructTaskConceptMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphStructTaskConceptMappingSaveReqVO 保存请求参数
     * @return GraphStructTaskConceptMappingPO
     */
     GraphStructTaskConceptMappingPO convertToPO(GraphStructTaskConceptMappingSaveReqVO graphStructTaskConceptMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphStructTaskConceptMappingPO 实体对象
     * @return GraphStructTaskConceptMappingRespVO
     */
     GraphStructTaskConceptMappingRespVO convertToRespVO(GraphStructTaskConceptMappingPO graphStructTaskConceptMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphStructTaskConceptMappingPOList 实体对象列表
     * @return List<GraphStructTaskConceptMappingRespVO>
     */
     List<GraphStructTaskConceptMappingRespVO> convertToRespVOList(List<GraphStructTaskConceptMappingPO> graphStructTaskConceptMappingPOList);
}
