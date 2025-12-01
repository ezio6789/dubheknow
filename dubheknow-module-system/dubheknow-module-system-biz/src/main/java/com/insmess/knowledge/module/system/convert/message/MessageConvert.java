package com.insmess.knowledge.module.system.convert.message;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageRespVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageDO;

import java.util.List;

/**
 * 消息 Convert
 *
 * @author insmess
 * @date 2024-10-31
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConvert {
    //MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param messagePageReqVO 请求参数
     * @return MessageDO
     */
     MessageDO convertToDO(MessagePageReqVO messagePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param messageSaveReqVO 保存请求参数
     * @return MessageDO
     */
     MessageDO convertToDO(MessageSaveReqVO messageSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param messageDO 实体对象
     * @return MessageRespVO
     */
     MessageRespVO convertToRespVO(MessageDO messageDO);

    /**
     * DOList 转换为 RespVOList
     * @param messageDOList 实体对象列表
     * @return List<MessageRespVO>
     */
     List<MessageRespVO> convertToRespVOList(List<MessageDO> messageDOList);
}
