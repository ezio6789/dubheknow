package com.insmess.knowledge.module.knowbase.convert.unstruct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentPO;

/**
 * 知识文件 Convert
 *
 * @author insmess
 * @date 2025-12-03
 */
@Mapper
public interface KnowbaseDocumentConvert {
    KnowbaseDocumentConvert INSTANCE = Mappers.getMapper(KnowbaseDocumentConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param knowbaseDocumentPageReqVO 请求参数
     * @return KnowbaseDocumentPO
     */
     KnowbaseDocumentPO convertToPO(KnowbaseDocumentPageReqVO knowbaseDocumentPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param knowbaseDocumentSaveReqVO 保存请求参数
     * @return KnowbaseDocumentPO
     */
     KnowbaseDocumentPO convertToPO(KnowbaseDocumentSaveReqVO knowbaseDocumentSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param knowbaseDocumentPO 实体对象
     * @return KnowbaseDocumentRespVO
     */
     KnowbaseDocumentRespVO convertToRespVO(KnowbaseDocumentPO knowbaseDocumentPO);

    /**
     * POList 转换为 RespVOList
     * @param knowbaseDocumentPOList 实体对象列表
     * @return List<KnowbaseDocumentRespVO>
     */
     List<KnowbaseDocumentRespVO> convertToRespVOList(List<KnowbaseDocumentPO> knowbaseDocumentPOList);
}
