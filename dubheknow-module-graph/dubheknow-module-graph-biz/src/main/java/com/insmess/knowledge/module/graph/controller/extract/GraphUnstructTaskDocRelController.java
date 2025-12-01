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
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelSaveReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskDocRelConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskDocRelPO;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskDocRelService;

/**
 * 任务文件关联Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "任务文件关联")
@RestController
@RequestMapping("/graph/extract/unstructTaskDocRel")
@Validated
public class GraphUnstructTaskDocRelController extends BaseController {
    @Resource
    private GraphUnstructTaskDocRelService graphUnstructTaskDocRelService;

    @Operation(summary = "查询任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphUnstructTaskDocRelRespVO>> list(GraphUnstructTaskDocRelPageReqVO graphUnstructTaskDocRel) {
        Page<GraphUnstructTaskDocRelPO> page = graphUnstructTaskDocRelService.pageGraphUnstructTaskDocRel(graphUnstructTaskDocRel);
        return CommonResult.success(BeanUtils.toBean(page, GraphUnstructTaskDocRelRespVO.class));
    }

    @Operation(summary = "导出任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:export')")
    @Log(title = "任务文件关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphUnstructTaskDocRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphUnstructTaskDocRelPO> list = (List<GraphUnstructTaskDocRelPO>) graphUnstructTaskDocRelService.pageGraphUnstructTaskDocRel(exportReqVO).getRecords();
        ExcelUtil<GraphUnstructTaskDocRelRespVO> util = new ExcelUtil<>(GraphUnstructTaskDocRelRespVO.class);
        util.exportExcel(response, GraphUnstructTaskDocRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:import')")
    @Log(title = "任务文件关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphUnstructTaskDocRelRespVO> util = new ExcelUtil<>(GraphUnstructTaskDocRelRespVO.class);
        List<GraphUnstructTaskDocRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphUnstructTaskDocRelService.importGraphUnstructTaskDocRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务文件关联详细信息")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphUnstructTaskDocRelRespVO> getInfo(@PathVariable("id") Long id) {
        GraphUnstructTaskDocRelPO graphUnstructTaskDocRelPO = graphUnstructTaskDocRelService.getGraphUnstructTaskDocRelById(id);
        return CommonResult.success(BeanUtils.toBean(graphUnstructTaskDocRelPO, GraphUnstructTaskDocRelRespVO.class));
    }

    @Operation(summary = "新增任务文件关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:add')")
    @Log(title = "任务文件关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphUnstructTaskDocRelSaveReqVO graphUnstructTaskDocRel) {
        return CommonResult.toAjax(graphUnstructTaskDocRelService.saveGraphUnstructTaskDocRel(graphUnstructTaskDocRel));
    }

    @Operation(summary = "修改任务文件关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:edit')")
    @Log(title = "任务文件关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphUnstructTaskDocRelSaveReqVO graphUnstructTaskDocRel) {
        return CommonResult.toAjax(graphUnstructTaskDocRelService.updateGraphUnstructTaskDocRel(graphUnstructTaskDocRel));
    }

    @Operation(summary = "删除任务文件关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskdocrel:remove')")
    @Log(title = "任务文件关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphUnstructTaskDocRelService.removeGraphUnstructTaskDocRel(Arrays.asList(ids)));
    }

}
