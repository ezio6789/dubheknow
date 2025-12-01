package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskPO;

/**
 * 结构化抽取任务 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphStructTaskConvert {
    GraphStructTaskConvert INSTANCE = Mappers.getMapper(GraphStructTaskConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphStructTaskPageReqVO 请求参数
     * @return GraphStructTaskPO
     */
     GraphStructTaskPO convertToPO(GraphStructTaskPageReqVO graphStructTaskPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphStructTaskSaveReqVO 保存请求参数
     * @return GraphStructTaskPO
     */
     GraphStructTaskPO convertToPO(GraphStructTaskSaveReqVO graphStructTaskSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphStructTaskPO 实体对象
     * @return GraphStructTaskRespVO
     */
     GraphStructTaskRespVO convertToRespVO(GraphStructTaskPO graphStructTaskPO);

    /**
     * POList 转换为 RespVOList
     * @param graphStructTaskPOList 实体对象列表
     * @return List<GraphStructTaskRespVO>
     */
     List<GraphStructTaskRespVO> convertToRespVOList(List<GraphStructTaskPO> graphStructTaskPOList);
}
