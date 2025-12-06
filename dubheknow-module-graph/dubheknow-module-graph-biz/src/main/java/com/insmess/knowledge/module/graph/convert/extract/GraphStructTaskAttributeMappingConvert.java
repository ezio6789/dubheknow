package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskAttributeMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingSaveReqVO;

/**
 * 属性映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphStructTaskAttributeMappingConvert {
    GraphStructTaskAttributeMappingConvert INSTANCE = Mappers.getMapper(GraphStructTaskAttributeMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphStructTaskAttributeMappingPageReqVO 请求参数
     * @return GraphStructTaskAttributeMappingPO
     */
     GraphStructTaskAttributeMappingPO convertToPO(GraphStructTaskAttributeMappingPageReqVO graphStructTaskAttributeMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphStructTaskAttributeMappingSaveReqVO 保存请求参数
     * @return GraphStructTaskAttributeMappingPO
     */
     GraphStructTaskAttributeMappingPO convertToPO(GraphStructTaskAttributeMappingSaveReqVO graphStructTaskAttributeMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphStructTaskAttributeMappingPO 实体对象
     * @return GraphStructTaskAttributeMappingRespVO
     */
     GraphStructTaskAttributeMappingRespVO convertToRespVO(GraphStructTaskAttributeMappingPO graphStructTaskAttributeMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphStructTaskAttributeMappingPOList 实体对象列表
     * @return List<GraphStructTaskAttributeMappingRespVO>
     */
     List<GraphStructTaskAttributeMappingRespVO> convertToRespVOList(List<GraphStructTaskAttributeMappingPO> graphStructTaskAttributeMappingPOList);
}
