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
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskPO;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskService;

/**
 * 结构化抽取任务Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "结构化抽取任务")
@RestController
@RequestMapping("/graph/extract/structTask")
@Validated
public class GraphStructTaskController extends BaseController {
    @Resource
    private GraphStructTaskService graphStructTaskService;

    @Operation(summary = "查询结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphStructTaskRespVO>> list(GraphStructTaskPageReqVO graphStructTask) {
        Page<GraphStructTaskPO> page = graphStructTaskService.pageGraphStructTask(graphStructTask);
        return CommonResult.success(BeanUtils.toBean(page, GraphStructTaskRespVO.class));
    }

    @Operation(summary = "导出结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:export')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphStructTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphStructTaskPO> list = (List<GraphStructTaskPO>) graphStructTaskService.pageGraphStructTask(exportReqVO).getRecords();
        ExcelUtil<GraphStructTaskRespVO> util = new ExcelUtil<>(GraphStructTaskRespVO.class);
        util.exportExcel(response, GraphStructTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:import')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphStructTaskRespVO> util = new ExcelUtil<>(GraphStructTaskRespVO.class);
        List<GraphStructTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphStructTaskService.importGraphStructTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取结构化抽取任务详细信息")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphStructTaskRespVO> getInfo(@PathVariable("id") Long id) {
        GraphStructTaskPO graphStructTaskPO = graphStructTaskService.getGraphStructTaskById(id);
        return CommonResult.success(BeanUtils.toBean(graphStructTaskPO, GraphStructTaskRespVO.class));
    }

    @Operation(summary = "新增结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:add')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphStructTaskSaveReqVO graphStructTask) {
        return CommonResult.toAjax(graphStructTaskService.saveGraphStructTask(graphStructTask));
    }

    @Operation(summary = "修改结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:edit')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphStructTaskSaveReqVO graphStructTask) {
        return CommonResult.toAjax(graphStructTaskService.updateGraphStructTask(graphStructTask));
    }

    @Operation(summary = "删除结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('graph:extract:structtask:remove')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphStructTaskService.removeGraphStructTask(Arrays.asList(ids)));
    }

}
