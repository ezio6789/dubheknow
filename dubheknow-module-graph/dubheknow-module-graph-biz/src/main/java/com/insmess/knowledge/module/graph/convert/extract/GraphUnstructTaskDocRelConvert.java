package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskDocRelPO;

/**
 * 任务文件关联 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphUnstructTaskDocRelConvert {
    GraphUnstructTaskDocRelConvert INSTANCE = Mappers.getMapper(GraphUnstructTaskDocRelConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphUnstructTaskDocRelPageReqVO 请求参数
     * @return GraphUnstructTaskDocRelPO
     */
     GraphUnstructTaskDocRelPO convertToPO(GraphUnstructTaskDocRelPageReqVO graphUnstructTaskDocRelPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphUnstructTaskDocRelSaveReqVO 保存请求参数
     * @return GraphUnstructTaskDocRelPO
     */
     GraphUnstructTaskDocRelPO convertToPO(GraphUnstructTaskDocRelSaveReqVO graphUnstructTaskDocRelSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphUnstructTaskDocRelPO 实体对象
     * @return GraphUnstructTaskDocRelRespVO
     */
     GraphUnstructTaskDocRelRespVO convertToRespVO(GraphUnstructTaskDocRelPO graphUnstructTaskDocRelPO);

    /**
     * POList 转换为 RespVOList
     * @param graphUnstructTaskDocRelPOList 实体对象列表
     * @return List<GraphUnstructTaskDocRelRespVO>
     */
     List<GraphUnstructTaskDocRelRespVO> convertToRespVOList(List<GraphUnstructTaskDocRelPO> graphUnstructTaskDocRelPOList);
}
