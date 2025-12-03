package com.insmess.knowledge.module.knowbase.convert.struct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourcePageReqVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceRespVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.struct.KnowbaseDatasourcePO;

/**
 * 数据源 Convert
 *
 * @author insmess
 * @date 2025-12-03
 */
@Mapper
public interface KnowbaseDatasourceConvert {
    KnowbaseDatasourceConvert INSTANCE = Mappers.getMapper(KnowbaseDatasourceConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param knowbaseDatasourcePageReqVO 请求参数
     * @return KnowbaseDatasourcePO
     */
     KnowbaseDatasourcePO convertToPO(KnowbaseDatasourcePageReqVO knowbaseDatasourcePageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param knowbaseDatasourceSaveReqVO 保存请求参数
     * @return KnowbaseDatasourcePO
     */
     KnowbaseDatasourcePO convertToPO(KnowbaseDatasourceSaveReqVO knowbaseDatasourceSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param knowbaseDatasourcePO 实体对象
     * @return KnowbaseDatasourceRespVO
     */
     KnowbaseDatasourceRespVO convertToRespVO(KnowbaseDatasourcePO knowbaseDatasourcePO);

    /**
     * POList 转换为 RespVOList
     * @param knowbaseDatasourcePOList 实体对象列表
     * @return List<KnowbaseDatasourceRespVO>
     */
     List<KnowbaseDatasourceRespVO> convertToRespVOList(List<KnowbaseDatasourcePO> knowbaseDatasourcePOList);
}
