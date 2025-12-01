package com.insmess.knowledge.module.system.controller.admin.system.message;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.insmess.knowledge.common.annotation.Log;
import com.insmess.knowledge.common.core.controller.BaseController;
import com.insmess.knowledge.common.core.domain.CommonResult;
import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.poi.ExcelUtil;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageRespVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import com.insmess.knowledge.module.system.convert.message.MessageConvert;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageDO;
import com.insmess.knowledge.module.system.dal.dataobject.message.enums.MessageHasReadEnums;
import com.insmess.knowledge.module.system.service.message.IMessageService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息Controller
 *
 * @author insmess
 * @date 2024-10-31
 */
@Tag(name = "消息")
@RestController
@RequestMapping("/system/message")
@Validated
@RequiredArgsConstructor
public class MessageController extends BaseController {
    private final IMessageService messageService;
    private final WebSocketMessageServer webSocketMessageServer;
    private final MessageConvert messageConvert;

    @Operation(summary = "查询消息列表")
//    @PreAuthorize("@ss.hasPermi('system:message:message:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<MessageRespVO>> list(MessagePageReqVO message) {
        startPage();
        PageResult<MessageDO> page = messageService.getMessagePage(message);
        return CommonResult.success(BeanUtils.toBean(page, MessageRespVO.class));
    }

    @Operation(summary = "导出消息列表")
    @PreAuthorize("@ss.hasPermi('system:message:message:export')")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessagePageReqVO message) {
        List<MessageDO> list = (List<MessageDO>) messageService.getMessagePage(message).getRows();
        ExcelUtil<MessageRespVO> util = new ExcelUtil<>(MessageRespVO.class);
        util.exportExcel(response, messageConvert.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取消息详细信息")
    @PreAuthorize("@ss.hasPermi('system:message:message:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<MessageRespVO> getInfo(@PathVariable("id") Long id) {
            MessageDO messageDO = messageService.getById(id);
        return CommonResult.success(BeanUtils.toBean(messageDO, MessageRespVO.class));
    }

    @Operation(summary = "新增消息")
    @PreAuthorize("@ss.hasPermi('system:message:message:add')")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody MessageSaveReqVO message) {
        MessageDO messageDO = BeanUtils.toBean(message, MessageDO.class);
        messageDO.setCreatorId(getUserId());
        messageDO.setCreateBy(getNickName());
        //通知在线用户有新消息
//        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
//        messagePageReqVO.setContent(messageDO.getContent());
//        messagePageReqVO.setTitle(messageDO.getTitle());
//        messagePageReqVO.setEntityType(null);
//        messagePageReqVO.setCreateTime(new Date());
//        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        return CommonResult.toAjax(messageService.save(messageDO));
    }

    @Operation(summary = "修改消息")
    @PreAuthorize("@ss.hasPermi('system:message:message:edit')")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@Valid @RequestBody MessageSaveReqVO message) {
        MessageDO messageDO = BeanUtils.toBean(message, MessageDO.class);
        messageDO.setUpdaterId(getUserId());
        messageDO.setUpdateBy(getNickName());
        messageDO.setUpdateTime(new Date());
        //通知在线用户有新消息
//        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
//        messagePageReqVO.setContent(messageDO.getContent());
//        messagePageReqVO.setTitle(messageDO.getTitle());
//        messagePageReqVO.setEntityType(null);
//        messagePageReqVO.setCreateTime(new Date());
//        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        return CommonResult.toAjax(messageService.updateById(messageDO));
    }

    @Operation(summary = "删除消息")
    @PreAuthorize("@ss.hasPermi('system:message:message:remove')")
    @Log(title = "消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Boolean> remove(@PathVariable("ids") Long[] ids) {
        boolean b = messageService.removeByIds(Arrays.asList(ids));
        messageService.getReceiverWDNum(getUserId());
        return CommonResult.toAjax(b);
    }

    @Operation(summary = "查询消息数量")
    //@PreAuthorize("@ss.hasPermi('system:message:message:list')")
    @GetMapping("/getNum")
    public CommonResult<Long> getNum(MessagePageReqVO message) {
        message.setHasRead(MessageHasReadEnums.WD.code);
        message.setReceiverId(getUserId());
        return CommonResult.success(messageService.getNum(message));
    }

    @Operation(summary = "已读消息")
    @PostMapping("/read")
    public CommonResult<Boolean> read(Long id) {
        return CommonResult.toAjax(messageService.read(id));
    }

    @Operation(summary = "全部消息已读")
    @PostMapping("/readAll")
    public CommonResult<Boolean> readAll(Integer category, Integer module) {
        return CommonResult.toAjax(messageService.readAll(getUserId(), category, module));
    }

    @Operation(summary = "测试添加消息")
    @GetMapping("/test")
    public CommonResult<Boolean> test(MessageSaveReqVO message, Long templateId, String context ) {
        Map<String, Object> map  = Maps.newLinkedHashMap();
        map.put("test", context);
        return CommonResult.success(messageService.send(templateId, message, map));
    }
}
