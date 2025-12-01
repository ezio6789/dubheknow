package com.insmess.knowledge.module.system.controller.admin.system.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageTemplateRespVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessageTemplateSaveReqVO;
import com.insmess.knowledge.module.system.convert.message.MessageTemplateConvert;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageTemplateDO;
import com.insmess.knowledge.module.system.service.message.IMessageTemplateService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 消息模板Controller
 *
 * @author insmess
 * @date 2024-10-31
 */
@Tag(name = "消息模板")
@RestController
@RequestMapping("/system/messageTemplate")
@Validated
public class MessageTemplateController extends BaseController {
    @Resource
    private IMessageTemplateService messageTemplateService;

    @Operation(summary = "查询消息模板列表")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<MessageTemplateRespVO>> list(MessageTemplatePageReqVO messageTemplate) {
        startPage();
        PageResult<MessageTemplateDO> page = messageTemplateService.getMessageTemplatePage(messageTemplate);
        return CommonResult.success(BeanUtils.toBean(page, MessageTemplateRespVO.class));
    }

    @Operation(summary = "导出消息模板列表")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:export')")
    @Log(title = "消息模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessageTemplatePageReqVO messageTemplate) {
        List<MessageTemplateDO> list = (List<MessageTemplateDO>) messageTemplateService.getMessageTemplatePage(messageTemplate).getRows();
        ExcelUtil<MessageTemplateRespVO> util = new ExcelUtil<>(MessageTemplateRespVO.class);
        util.exportExcel(response, MessageTemplateConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取消息模板详细信息")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<MessageTemplateRespVO> getInfo(@PathVariable("id") Long id) {
            MessageTemplateDO messageTemplateDO = messageTemplateService.getById(id);
        return CommonResult.success(BeanUtils.toBean(messageTemplateDO, MessageTemplateRespVO.class));
    }

    @Operation(summary = "新增消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:add')")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody MessageTemplateSaveReqVO messageTemplate) {
        MessageTemplateDO messageTemplateDO = BeanUtils.toBean(messageTemplate, MessageTemplateDO.class);
        messageTemplateDO.setCreatorId(getUserId());
        messageTemplateDO.setCreateBy(getNickName());
        return CommonResult.toAjax(messageTemplateService.save(messageTemplateDO));
    }

    @Operation(summary = "修改消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:edit')")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@Valid @RequestBody MessageTemplateSaveReqVO messageTemplate) {
        MessageTemplateDO messageTemplateDO = BeanUtils.toBean(messageTemplate, MessageTemplateDO.class);
        messageTemplateDO.setUpdaterId(getUserId());
        messageTemplateDO.setUpdateBy(getNickName());
        messageTemplateDO.setUpdateTime(new Date());
        return CommonResult.toAjax(messageTemplateService.updateById(messageTemplateDO));
    }

    @Operation(summary = "删除消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:remove')")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Boolean> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(messageTemplateService.removeByIds(Arrays.asList(ids)));
    }
}
