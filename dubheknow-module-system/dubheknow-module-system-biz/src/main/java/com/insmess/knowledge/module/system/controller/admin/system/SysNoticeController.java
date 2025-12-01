package com.insmess.knowledge.module.system.controller.admin.system;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.insmess.knowledge.common.annotation.Log;
import com.insmess.knowledge.common.core.controller.BaseController;
import com.insmess.knowledge.common.core.domain.AjaxResult;
import com.insmess.knowledge.common.core.page.TableDataInfo;
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import com.insmess.knowledge.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import com.insmess.knowledge.module.system.dal.dataobject.message.MessageDO;
import com.insmess.knowledge.module.system.domain.SysNotice;
import com.insmess.knowledge.module.system.service.ISysNoticeService;
import com.insmess.knowledge.module.system.service.message.IMessageService;

import jakarta.annotation.Resource;

/**
 * 公告 信息操作处理
 *
 * @author insmess
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;
    @Resource
    private WebSocketMessageServer webSocketMessageServer;
    @Resource
    private IMessageService messageService;

    /**
     * 获取通知公告列表
     */
//    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        //测试 消息通知
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setContent(notice.getNoticeContent());
        messagePageReqVO.setTitle(notice.getNoticeTitle());
        messagePageReqVO.setEntityType(Integer.valueOf(notice.getNoticeType()));
        messagePageReqVO.setCreateTime(new Date());
        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        notice.setCreateBy(getNickName());

        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getNickName());
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setContent(notice.getNoticeContent());
        messagePageReqVO.setTitle(notice.getNoticeTitle());
        messagePageReqVO.setEntityType(Integer.valueOf(notice.getNoticeType()));
        messagePageReqVO.setCreateTime(new Date());
        webSocketMessageServer.broadcastMessage(messagePageReqVO);

        MessageDO messageDO = new MessageDO();
        // 设置模版基本数据
        messageDO.setCategory(Integer.valueOf(0));
        messageDO.setMsgLevel(Integer.valueOf(0));
        messageDO.setTitle("测试");
        // 实际消息
        messageDO.setContent("测试内容");

//        messageDO.setCreatorId(getLoginUser().getUserId());
//        messageDO.setCreateBy(getLoginUser().getUser().getNickName());
        boolean save = messageService.save(messageDO);

        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable("noticeIds") Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
