package com.insmess.knowledge.module.knowbase.convert.unstruct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentContentPO;

/**
 * 知识内容 Convert
 *
 * @author insmess
 * @date 2025-12-03
 */
@Mapper
public interface KnowbaseDocumentContentConvert {
    KnowbaseDocumentContentConvert INSTANCE = Mappers.getMapper(KnowbaseDocumentContentConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param knowbaseDocumentContentPageReqVO 请求参数
     * @return KnowbaseDocumentContentPO
     */
     KnowbaseDocumentContentPO convertToPO(KnowbaseDocumentContentPageReqVO knowbaseDocumentContentPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param knowbaseDocumentContentSaveReqVO 保存请求参数
     * @return KnowbaseDocumentContentPO
     */
     KnowbaseDocumentContentPO convertToPO(KnowbaseDocumentContentSaveReqVO knowbaseDocumentContentSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param knowbaseDocumentContentPO 实体对象
     * @return KnowbaseDocumentContentRespVO
     */
     KnowbaseDocumentContentRespVO convertToRespVO(KnowbaseDocumentContentPO knowbaseDocumentContentPO);

    /**
     * POList 转换为 RespVOList
     * @param knowbaseDocumentContentPOList 实体对象列表
     * @return List<KnowbaseDocumentContentRespVO>
     */
     List<KnowbaseDocumentContentRespVO> convertToRespVOList(List<KnowbaseDocumentContentPO> knowbaseDocumentContentPOList);
}
