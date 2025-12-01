package com.insmess.knowledge.module.system.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.insmess.knowledge.common.core.page.PageResult;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.insmess.knowledge.common.exception.base.BaseException;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import com.insmess.knowledge.module.system.convert.message.MessageConvert;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageDO;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageTemplateDO;
import com.insmess.knowledge.module.system.dal.dataobject.message.enums.MessageHasReadEnums;
import com.insmess.knowledge.module.system.dal.mapper.message.MessageMapper;
import com.insmess.knowledge.module.system.dal.mapper.message.MessageTemplateMapper;
import com.insmess.knowledge.module.system.service.message.IMessageService;

import java.util.List;
import java.util.Map;

import static com.insmess.knowledge.common.utils.SecurityUtils.getLoginUser;

/**
 * 消息Service业务层处理
 *
 * @author insmess
 * @date 2024-10-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MessageServiceImpl  extends ServiceImpl<MessageMapper, MessageDO> implements IMessageService {
    private final MessageMapper messageMapper;
    private final MessageTemplateMapper messageTemplateMapper;

    private final MessageConvert messageConvert;

    @Override
    public PageResult<MessageDO> getMessagePage(MessagePageReqVO message) {
        QueryWrapper<MessageDO> queryWrapper = new QueryWrapper<>(messageConvert.convertToDO(message));
        // 添加时间范围查询条件
        if (message.getStartTime() != null && message.getEndTime() != null) {
            queryWrapper.ge("DATE(create_time)", message.getStartTime()); // >= 起始时间
            queryWrapper.le("DATE(create_time)", message.getEndTime()); // <= 结束时间
        }
        List<MessageDO> list = list(queryWrapper);

        return new PageResult(list, new PageInfo(list).getTotal());
    }

    /**
     * 通过模版向某一个用户发送消息
     * @param templateId 模版id
     * @param messageSaveReqVO 消息创建
     * @param entity 实体对象
     * @return 是否发送成功
     */
    @Override
    public Boolean send(Long templateId, MessageSaveReqVO messageSaveReqVO, Object entity) {
        MessageTemplateDO messageTemplateDO = messageTemplateMapper.selectById(templateId); // 获取对应的模版
        if (messageTemplateDO == null) {
            throw new BaseException("消息模版不存在");
        }
        Map<?, ?> map = BeanUtils.toBean(entity, Map.class); // 将实体转换为键值对
        // 消息模版更新数值
        String message = messageTemplateDO.getContent();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            message = message.replace("${" + entry.getKey().toString() + "}", entry.getValue().toString());
        }
        MessageDO messageDO = BeanUtils.toBean(messageSaveReqVO, MessageDO.class);
        // 设置模版基本数据
        messageDO.setCategory(messageTemplateDO.getCategory());
        messageDO.setMsgLevel(messageTemplateDO.getMsgLevel());
        messageDO.setTitle(messageTemplateDO.getTitle());
        // 实际消息
        messageDO.setContent(message);

        messageDO.setCreatorId(getLoginUser().getUserId());
        messageDO.setCreateBy(getLoginUser().getUser().getNickName());
        boolean save = this.save(messageDO);
        // 更新消息
        this.getReceiverWDNum(messageSaveReqVO.getReceiverId());
        return save;
    }

    /**
     * 查询消息数量
     * @param message 查询条件
     * @return 数量
     */
    @Override
    public Long getNum(MessagePageReqVO message) {
        message.setDelFlag(1);
        QueryWrapper<MessageDO> queryWrapper = new QueryWrapper<>(messageConvert.convertToDO(message));
        Long count = this.count(queryWrapper);
        WebSocketMessageServer.sendMessage(message.getReceiverId().toString(), count.toString()); // 消息更新
        return count;
    }

    /**
     * 设置已读
     * @param id 消息id
     * @return 是否成功
     */
    @Override
    public Boolean read(Long id) {
        MessageDO messageDO = new MessageDO();
        messageDO.setId(id);
        messageDO.setHasRead(MessageHasReadEnums.YD.code);
        boolean b = this.updateById(messageDO);
        // 更新消息
        this.getReceiverWDNum(getLoginUser().getUserId());
        return b;
    }

    /**
     * 全部已读
     * @param receiverId 接收人id
     * @param category 消息类型
     * @param module 消息模块
     * @return 是否成功
     */
    @Override
    public Boolean readAll(Long receiverId, Integer category, Integer module) {
        LambdaUpdateWrapper<MessageDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MessageDO::getReceiverId, receiverId);
        if (category != null) {
            updateWrapper.eq(MessageDO::getCategory, category);
        }
        if (module != null) {
            updateWrapper.eq(MessageDO::getModule, module);
        }
        updateWrapper.set(MessageDO::getHasRead, MessageHasReadEnums.YD.code);
        boolean update = this.update(updateWrapper);
        // 更新消息
        this.getReceiverWDNum(getLoginUser().getUserId());
        return update;
    }

    /**
     * 更新接收人未读消息
     *
     * @param receiverId 接收人id
     */
    @Override
    public void getReceiverWDNum(Long receiverId) {
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setHasRead(MessageHasReadEnums.WD.code);
        messagePageReqVO.setReceiverId(receiverId);
        this.getNum(messagePageReqVO);
    }
}
