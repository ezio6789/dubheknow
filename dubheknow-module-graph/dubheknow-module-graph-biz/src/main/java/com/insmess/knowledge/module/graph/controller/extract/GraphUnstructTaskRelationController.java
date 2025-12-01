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
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationSaveReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskRelationConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskRelationPO;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskRelationService;

/**
 * 任务关系关联Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "任务关系关联")
@RestController
@RequestMapping("/graph/extract/unstructTaskRelation")
@Validated
public class GraphUnstructTaskRelationController extends BaseController {
    @Resource
    private GraphUnstructTaskRelationService graphUnstructTaskRelationService;

    @Operation(summary = "查询任务关系关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphUnstructTaskRelationRespVO>> list(GraphUnstructTaskRelationPageReqVO graphUnstructTaskRelation) {
        Page<GraphUnstructTaskRelationPO> page = graphUnstructTaskRelationService.pageGraphUnstructTaskRelation(graphUnstructTaskRelation);
        return CommonResult.success(BeanUtils.toBean(page, GraphUnstructTaskRelationRespVO.class));
    }

    @Operation(summary = "导出任务关系关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:export')")
    @Log(title = "任务关系关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphUnstructTaskRelationPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphUnstructTaskRelationPO> list = (List<GraphUnstructTaskRelationPO>) graphUnstructTaskRelationService.pageGraphUnstructTaskRelation(exportReqVO).getRecords();
        ExcelUtil<GraphUnstructTaskRelationRespVO> util = new ExcelUtil<>(GraphUnstructTaskRelationRespVO.class);
        util.exportExcel(response, GraphUnstructTaskRelationConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务关系关联列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:import')")
    @Log(title = "任务关系关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphUnstructTaskRelationRespVO> util = new ExcelUtil<>(GraphUnstructTaskRelationRespVO.class);
        List<GraphUnstructTaskRelationRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphUnstructTaskRelationService.importGraphUnstructTaskRelation(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务关系关联详细信息")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphUnstructTaskRelationRespVO> getInfo(@PathVariable("id") Long id) {
        GraphUnstructTaskRelationPO graphUnstructTaskRelationPO = graphUnstructTaskRelationService.getGraphUnstructTaskRelationById(id);
        return CommonResult.success(BeanUtils.toBean(graphUnstructTaskRelationPO, GraphUnstructTaskRelationRespVO.class));
    }

    @Operation(summary = "新增任务关系关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:add')")
    @Log(title = "任务关系关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphUnstructTaskRelationSaveReqVO graphUnstructTaskRelation) {
        return CommonResult.toAjax(graphUnstructTaskRelationService.saveGraphUnstructTaskRelation(graphUnstructTaskRelation));
    }

    @Operation(summary = "修改任务关系关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:edit')")
    @Log(title = "任务关系关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphUnstructTaskRelationSaveReqVO graphUnstructTaskRelation) {
        return CommonResult.toAjax(graphUnstructTaskRelationService.updateGraphUnstructTaskRelation(graphUnstructTaskRelation));
    }

    @Operation(summary = "删除任务关系关联")
    @PreAuthorize("@ss.hasPermi('graph:extract:unstructtaskrelation:remove')")
    @Log(title = "任务关系关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphUnstructTaskRelationService.removeGraphUnstructTaskRelation(Arrays.asList(ids)));
    }

}
