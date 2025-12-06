package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;

import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptPageReqVO;

/**
 * 概念配置 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphConceptConvert {
    GraphConceptConvert INSTANCE = Mappers.getMapper(GraphConceptConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphConceptPageReqVO 请求参数
     * @return GraphConceptPO
     */
     GraphConceptPO convertToPO(GraphConceptPageReqVO graphConceptPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphConceptSaveReqVO 保存请求参数
     * @return GraphConceptPO
     */
     GraphConceptPO convertToPO(GraphConceptSaveReqVO graphConceptSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphConceptPO 实体对象
     * @return GraphConceptRespVO
     */
     GraphConceptRespVO convertToRespVO(GraphConceptPO graphConceptPO);

    /**
     * POList 转换为 RespVOList
     * @param graphConceptPOList 实体对象列表
     * @return List<GraphConceptRespVO>
     */
     List<GraphConceptRespVO> convertToRespVOList(List<GraphConceptPO> graphConceptPOList);
}
