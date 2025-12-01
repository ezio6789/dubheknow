package com.insmess.knowledge.module.graph.convert.extract;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskTextPO;

/**
 * 任务文件段落关联 Convert
 *
 * @author insmess
 * @date 2025-11-29
 */
@Mapper
public interface GraphUnstructTaskTextConvert {
    GraphUnstructTaskTextConvert INSTANCE = Mappers.getMapper(GraphUnstructTaskTextConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param graphUnstructTaskTextPageReqVO 请求参数
     * @return GraphUnstructTaskTextPO
     */
     GraphUnstructTaskTextPO convertToPO(GraphUnstructTaskTextPageReqVO graphUnstructTaskTextPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param graphUnstructTaskTextSaveReqVO 保存请求参数
     * @return GraphUnstructTaskTextPO
     */
     GraphUnstructTaskTextPO convertToPO(GraphUnstructTaskTextSaveReqVO graphUnstructTaskTextSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param graphUnstructTaskTextPO 实体对象
     * @return GraphUnstructTaskTextRespVO
     */
     GraphUnstructTaskTextRespVO convertToRespVO(GraphUnstructTaskTextPO graphUnstructTaskTextPO);

    /**
     * POList 转换为 RespVOList
     * @param graphUnstructTaskTextPOList 实体对象列表
     * @return List<GraphUnstructTaskTextRespVO>
     */
     List<GraphUnstructTaskTextRespVO> convertToRespVOList(List<GraphUnstructTaskTextPO> graphUnstructTaskTextPOList);
}
