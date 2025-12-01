package com.insmess.knowledge.module.graph.convert.ontology;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaAttributePO;

/**
 * 概念属性 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphSchemaAttributeConvert {
    GraphSchemaAttributeConvert INSTANCE = Mappers.getMapper(GraphSchemaAttributeConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphSchemaAttributePageReqVO 请求参数
     * @return GraphSchemaAttributePO
     */
     GraphSchemaAttributePO convertToPO(GraphSchemaAttributePageReqVO graphSchemaAttributePageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphSchemaAttributeSaveReqVO 保存请求参数
     * @return GraphSchemaAttributePO
     */
     GraphSchemaAttributePO convertToPO(GraphSchemaAttributeSaveReqVO graphSchemaAttributeSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphSchemaAttributePO 实体对象
     * @return GraphSchemaAttributeRespVO
     */
     GraphSchemaAttributeRespVO convertToRespVO(GraphSchemaAttributePO graphSchemaAttributePO);

    /**
     * POList 转换为 RespVOList
     * @param graphSchemaAttributePOList 实体对象列表
     * @return List<GraphSchemaAttributeRespVO>
     */
     List<GraphSchemaAttributeRespVO> convertToRespVOList(List<GraphSchemaAttributePO> graphSchemaAttributePOList);
}
