package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphAttributeMappingPO;

/**
 * 属性映射 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphAttributeMappingConvert {
    GraphAttributeMappingConvert INSTANCE = Mappers.getMapper(GraphAttributeMappingConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphAttributeMappingPageReqVO 请求参数
     * @return GraphAttributeMappingPO
     */
     GraphAttributeMappingPO convertToPO(GraphAttributeMappingPageReqVO graphAttributeMappingPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphAttributeMappingSaveReqVO 保存请求参数
     * @return GraphAttributeMappingPO
     */
     GraphAttributeMappingPO convertToPO(GraphAttributeMappingSaveReqVO graphAttributeMappingSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphAttributeMappingPO 实体对象
     * @return GraphAttributeMappingRespVO
     */
     GraphAttributeMappingRespVO convertToRespVO(GraphAttributeMappingPO graphAttributeMappingPO);

    /**
     * POList 转换为 RespVOList
     * @param graphAttributeMappingPOList 实体对象列表
     * @return List<GraphAttributeMappingRespVO>
     */
     List<GraphAttributeMappingRespVO> convertToRespVOList(List<GraphAttributeMappingPO> graphAttributeMappingPOList);
}
