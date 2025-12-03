package com.insmess.knowledge.module.knowbase.convert.unstruct;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentDirPO;

/**
 * 知识目录 Convert
 *
 * @author insmess
 * @date 2025-12-03
 */
@Mapper
public interface KnowbaseDocumentDirConvert {
    KnowbaseDocumentDirConvert INSTANCE = Mappers.getMapper(KnowbaseDocumentDirConvert.class);

    /**
     * PageReqVO 转换为 PO
     * @param knowbaseDocumentDirPageReqVO 请求参数
     * @return KnowbaseDocumentDirPO
     */
     KnowbaseDocumentDirPO convertToPO(KnowbaseDocumentDirPageReqVO knowbaseDocumentDirPageReqVO);

    /**
     * SaveReqVO 转换为 PO
     * @param knowbaseDocumentDirSaveReqVO 保存请求参数
     * @return KnowbaseDocumentDirPO
     */
     KnowbaseDocumentDirPO convertToPO(KnowbaseDocumentDirSaveReqVO knowbaseDocumentDirSaveReqVO);

    /**
     * PO 转换为 RespVO
     * @param knowbaseDocumentDirPO 实体对象
     * @return KnowbaseDocumentDirRespVO
     */
     KnowbaseDocumentDirRespVO convertToRespVO(KnowbaseDocumentDirPO knowbaseDocumentDirPO);

    /**
     * POList 转换为 RespVOList
     * @param knowbaseDocumentDirPOList 实体对象列表
     * @return List<KnowbaseDocumentDirRespVO>
     */
     List<KnowbaseDocumentDirRespVO> convertToRespVOList(List<KnowbaseDocumentDirPO> knowbaseDocumentDirPOList);
}
