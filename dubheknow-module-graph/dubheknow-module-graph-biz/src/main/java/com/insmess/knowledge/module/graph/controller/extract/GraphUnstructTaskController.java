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
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskPO;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskService;

/**
 * 非结构化抽取任务Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "非结构化抽取任务")
@RestController
@RequestMapping("/graph/extract/unstructTask")
@Validated
public class GraphUnstructTaskController extends BaseController {
    @Resource
    private GraphUnstructTaskService graphUnstructTaskService;

    @Operation(summary = "查询非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphUnstructTaskRespVO>> list(GraphUnstructTaskPageReqVO graphUnstructTask) {
        Page<GraphUnstructTaskPO> page = graphUnstructTaskService.pageGraphUnstructTask(graphUnstructTask);
        return CommonResult.success(BeanUtils.toBean(page, GraphUnstructTaskRespVO.class));
    }

    @Operation(summary = "导出非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:export')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphUnstructTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphUnstructTaskPO> list = (List<GraphUnstructTaskPO>) graphUnstructTaskService.pageGraphUnstructTask(exportReqVO).getRecords();
        ExcelUtil<GraphUnstructTaskRespVO> util = new ExcelUtil<>(GraphUnstructTaskRespVO.class);
        util.exportExcel(response, GraphUnstructTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:import')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphUnstructTaskRespVO> util = new ExcelUtil<>(GraphUnstructTaskRespVO.class);
        List<GraphUnstructTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphUnstructTaskService.importGraphUnstructTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取非结构化抽取任务详细信息")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphUnstructTaskRespVO> getInfo(@PathVariable("id") Long id) {
        GraphUnstructTaskPO graphUnstructTaskPO = graphUnstructTaskService.getGraphUnstructTaskById(id);
        return CommonResult.success(BeanUtils.toBean(graphUnstructTaskPO, GraphUnstructTaskRespVO.class));
    }

    @Operation(summary = "新增非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:add')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphUnstructTaskSaveReqVO graphUnstructTask) {
        return CommonResult.toAjax(graphUnstructTaskService.saveGraphUnstructTask(graphUnstructTask));
    }

    @Operation(summary = "修改非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:edit')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphUnstructTaskSaveReqVO graphUnstructTask) {
        return CommonResult.toAjax(graphUnstructTaskService.updateGraphUnstructTask(graphUnstructTask));
    }

    @Operation(summary = "删除非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtask:remove')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphUnstructTaskService.removeGraphUnstructTask(Arrays.asList(ids)));
    }

}
