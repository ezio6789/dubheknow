package com.insmess.knowledge.module.system.service.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import com.insmess.knowledge.module.system.convert.message.MessageTemplateConvert;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageTemplateDO;

import java.util.List;

/**
 * 消息模板Service接口
 *
 * @author insmess
 * @date 2024-10-31
 */
public interface IMessageTemplateService extends IService<MessageTemplateDO> {

    default PageResult<MessageTemplateDO> getMessageTemplatePage(MessageTemplatePageReqVO messageTemplate) {
        QueryWrapper<MessageTemplateDO> queryWrapper = new QueryWrapper<>(MessageTemplateConvert.INSTANCE.convertToDO(messageTemplate));
        List<MessageTemplateDO> list = list(queryWrapper);

        return new PageResult(list, new PageInfo(list).getTotal());
    }
}
