package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskRelationPO;

/**
 * 任务关系关联 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphUnstructTaskRelationConvert {
    GraphUnstructTaskRelationConvert INSTANCE = Mappers.getMapper(GraphUnstructTaskRelationConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphUnstructTaskRelationPageReqVO 请求参数
     * @return GraphUnstructTaskRelationPO
     */
     GraphUnstructTaskRelationPO convertToPO(GraphUnstructTaskRelationPageReqVO graphUnstructTaskRelationPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphUnstructTaskRelationSaveReqVO 保存请求参数
     * @return GraphUnstructTaskRelationPO
     */
     GraphUnstructTaskRelationPO convertToPO(GraphUnstructTaskRelationSaveReqVO graphUnstructTaskRelationSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphUnstructTaskRelationPO 实体对象
     * @return GraphUnstructTaskRelationRespVO
     */
     GraphUnstructTaskRelationRespVO convertToRespVO(GraphUnstructTaskRelationPO graphUnstructTaskRelationPO);

    /**
     * POList 转换为 RespVOList
     * @param graphUnstructTaskRelationPOList 实体对象列表
     * @return List<GraphUnstructTaskRelationRespVO>
     */
     List<GraphUnstructTaskRelationRespVO> convertToRespVOList(List<GraphUnstructTaskRelationPO> graphUnstructTaskRelationPOList);
}
