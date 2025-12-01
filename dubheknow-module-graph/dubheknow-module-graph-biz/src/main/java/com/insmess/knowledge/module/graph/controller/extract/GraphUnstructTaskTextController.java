package com.insmess.knowledge.module.graph.controller.extract;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.common.core.page.PageParam;
import com.insmess.knowledge.common.core.domain.AjaxResult;
import com.insmess.knowledge.common.annotation.Log;
import com.insmess.knowledge.common.core.controller.BaseController;
import com.insmess.knowledge.common.core.domain.CommonResult;
import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.poi.ExcelUtil;
import com.insmess.knowledge.common.exception.enums.GlobalErrorCodeConstants;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextSaveReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskTextConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskTextPO;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskTextService;

/**
 * 任务文件段落关联Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "任务文件段落关联")
@RestController
@RequestMapping("/graph/extract/unstructTaskText")
@Validated
public class GraphUnstructTaskTextController extends BaseController {
    @Resource
    private GraphUnstructTaskTextService graphUnstructTaskTextService;

    @Operation(summary = "查询任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphUnstructTaskTextRespVO>> list(GraphUnstructTaskTextPageReqVO graphUnstructTaskText) {
        Page<GraphUnstructTaskTextPO> page = graphUnstructTaskTextService.pageGraphUnstructTaskText(graphUnstructTaskText);
        return CommonResult.success(BeanUtils.toBean(page, GraphUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "导出任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:export')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphUnstructTaskTextPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphUnstructTaskTextPO> list = (List<GraphUnstructTaskTextPO>) graphUnstructTaskTextService.pageGraphUnstructTaskText(exportReqVO).getRecords();
        ExcelUtil<GraphUnstructTaskTextRespVO> util = new ExcelUtil<>(GraphUnstructTaskTextRespVO.class);
        util.exportExcel(response, GraphUnstructTaskTextConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:import')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphUnstructTaskTextRespVO> util = new ExcelUtil<>(GraphUnstructTaskTextRespVO.class);
        List<GraphUnstructTaskTextRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphUnstructTaskTextService.importGraphUnstructTaskText(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务文件段落关联详细信息")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphUnstructTaskTextRespVO> getInfo(@PathVariable("id") Long id) {
        GraphUnstructTaskTextPO graphUnstructTaskTextPO = graphUnstructTaskTextService.getGraphUnstructTaskTextById(id);
        return CommonResult.success(BeanUtils.toBean(graphUnstructTaskTextPO, GraphUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "新增任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:add')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphUnstructTaskTextSaveReqVO graphUnstructTaskText) {
        return CommonResult.toAjax(graphUnstructTaskTextService.saveGraphUnstructTaskText(graphUnstructTaskText));
    }

    @Operation(summary = "修改任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:edit')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphUnstructTaskTextSaveReqVO graphUnstructTaskText) {
        return CommonResult.toAjax(graphUnstructTaskTextService.updateGraphUnstructTaskText(graphUnstructTaskText));
    }

    @Operation(summary = "删除任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtasktext:remove')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphUnstructTaskTextService.removeGraphUnstructTaskText(Arrays.asList(ids)));
    }

}
