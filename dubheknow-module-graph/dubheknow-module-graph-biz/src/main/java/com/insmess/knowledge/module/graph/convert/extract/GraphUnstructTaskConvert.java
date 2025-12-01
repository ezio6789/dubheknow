package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskPO;

/**
 * 非结构化抽取任务 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphUnstructTaskConvert {
    GraphUnstructTaskConvert INSTANCE = Mappers.getMapper(GraphUnstructTaskConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphUnstructTaskPageReqVO 请求参数
     * @return GraphUnstructTaskPO
     */
     GraphUnstructTaskPO convertToPO(GraphUnstructTaskPageReqVO graphUnstructTaskPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphUnstructTaskSaveReqVO 保存请求参数
     * @return GraphUnstructTaskPO
     */
     GraphUnstructTaskPO convertToPO(GraphUnstructTaskSaveReqVO graphUnstructTaskSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphUnstructTaskPO 实体对象
     * @return GraphUnstructTaskRespVO
     */
     GraphUnstructTaskRespVO convertToRespVO(GraphUnstructTaskPO graphUnstructTaskPO);

    /**
     * POList 转换为 RespVOList
     * @param graphUnstructTaskPOList 实体对象列表
     * @return List<GraphUnstructTaskRespVO>
     */
     List<GraphUnstructTaskRespVO> convertToRespVOList(List<GraphUnstructTaskPO> graphUnstructTaskPOList);
}
