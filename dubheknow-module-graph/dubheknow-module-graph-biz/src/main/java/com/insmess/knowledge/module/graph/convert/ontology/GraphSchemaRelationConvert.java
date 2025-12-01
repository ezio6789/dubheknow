package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaRelationPO;

/**
 * 关系配置 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphSchemaRelationConvert {
    GraphSchemaRelationConvert INSTANCE = Mappers.getMapper(GraphSchemaRelationConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphSchemaRelationPageReqVO 请求参数
     * @return GraphSchemaRelationPO
     */
     GraphSchemaRelationPO convertToPO(GraphSchemaRelationPageReqVO graphSchemaRelationPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphSchemaRelationSaveReqVO 保存请求参数
     * @return GraphSchemaRelationPO
     */
     GraphSchemaRelationPO convertToPO(GraphSchemaRelationSaveReqVO graphSchemaRelationSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphSchemaRelationPO 实体对象
     * @return GraphSchemaRelationRespVO
     */
     GraphSchemaRelationRespVO convertToRespVO(GraphSchemaRelationPO graphSchemaRelationPO);

    /**
     * POList 转换为 RespVOList
     * @param graphSchemaRelationPOList 实体对象列表
     * @return List<GraphSchemaRelationRespVO>
     */
     List<GraphSchemaRelationRespVO> convertToRespVOList(List<GraphSchemaRelationPO> graphSchemaRelationPOList);
}
