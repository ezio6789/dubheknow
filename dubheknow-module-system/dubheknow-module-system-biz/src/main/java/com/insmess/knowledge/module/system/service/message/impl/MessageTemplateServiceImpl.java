package com.insmess.knowledge.module.system.service.message.impl;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageTemplateDO;
import com.insmess.knowledge.module.system.dal.mapper.message.MessageTemplateMapper;
import com.insmess.knowledge.module.system.service.message.IMessageTemplateService;

/**
 * 消息模板Service业务层处理
 *
 * @author insmess
 * @date 2024-10-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTemplateServiceImpl  extends ServiceImpl<MessageTemplateMapper,MessageTemplateDO> implements IMessageTemplateService {
    @Resource
    private MessageTemplateMapper messageTemplateMapper;

}
