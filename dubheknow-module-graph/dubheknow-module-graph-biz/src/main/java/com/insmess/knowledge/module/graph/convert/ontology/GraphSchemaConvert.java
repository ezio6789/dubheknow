package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaPO;

/**
 * 概念配置 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphSchemaConvert {
    GraphSchemaConvert INSTANCE = Mappers.getMapper(GraphSchemaConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphSchemaPageReqVO 请求参数
     * @return GraphSchemaPO
     */
     GraphSchemaPO convertToPO(GraphSchemaPageReqVO graphSchemaPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphSchemaSaveReqVO 保存请求参数
     * @return GraphSchemaPO
     */
     GraphSchemaPO convertToPO(GraphSchemaSaveReqVO graphSchemaSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphSchemaPO 实体对象
     * @return GraphSchemaRespVO
     */
     GraphSchemaRespVO convertToRespVO(GraphSchemaPO graphSchemaPO);

    /**
     * POList 转换为 RespVOList
     * @param graphSchemaPOList 实体对象列表
     * @return List<GraphSchemaRespVO>
     */
     List<GraphSchemaRespVO> convertToRespVOList(List<GraphSchemaPO> graphSchemaPOList);
}
