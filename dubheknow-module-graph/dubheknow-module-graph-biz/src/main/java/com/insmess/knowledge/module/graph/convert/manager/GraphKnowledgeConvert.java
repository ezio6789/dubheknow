package com.insmess.knowledge.module.graph.convert.manager;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgePageReqVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeRespVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.manager.GraphKnowledgePO;

/**
 * 知识图谱 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphKnowledgeConvert {
    GraphKnowledgeConvert INSTANCE = Mappers.getMapper(GraphKnowledgeConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphKnowledgePageReqVO 请求参数
     * @return GraphKnowledgePO
     */
     GraphKnowledgePO convertToPO(GraphKnowledgePageReqVO graphKnowledgePageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphKnowledgeSaveReqVO 保存请求参数
     * @return GraphKnowledgePO
     */
     GraphKnowledgePO convertToPO(GraphKnowledgeSaveReqVO graphKnowledgeSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphKnowledgePO 实体对象
     * @return GraphKnowledgeRespVO
     */
     GraphKnowledgeRespVO convertToRespVO(GraphKnowledgePO graphKnowledgePO);

    /**
     * POList 转换为 RespVOList
     * @param graphKnowledgePOList 实体对象列表
     * @return List<GraphKnowledgeRespVO>
     */
     List<GraphKnowledgeRespVO> convertToRespVOList(List<GraphKnowledgePO> graphKnowledgePOList);
}
