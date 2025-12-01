package com.insmess.knowledge.module.graph.controller.manager;

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
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgePageReqVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeRespVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeSaveReqVO;
import com.insmess.knowledge.module.graph.convert.manager.GraphKnowledgeConvert;
import com.insmess.knowledge.module.graph.dao.po.manager.GraphKnowledgePO;
import com.insmess.knowledge.module.graph.service.manager.GraphKnowledgeService;

/**
 * 知识图谱Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "知识图谱")
@RestController
@RequestMapping("/graph/manager/knowledge")
@Validated
public class GraphKnowledgeController extends BaseController {
    @Resource
    private GraphKnowledgeService graphKnowledgeService;

    @Operation(summary = "查询知识图谱列表")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphKnowledgeRespVO>> list(GraphKnowledgePageReqVO graphKnowledge) {
        Page<GraphKnowledgePO> page = graphKnowledgeService.pageGraphKnowledge(graphKnowledge);
        return CommonResult.success(BeanUtils.toBean(page, GraphKnowledgeRespVO.class));
    }

    @Operation(summary = "导出知识图谱列表")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:export')")
    @Log(title = "知识图谱", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphKnowledgePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphKnowledgePO> list = (List<GraphKnowledgePO>) graphKnowledgeService.pageGraphKnowledge(exportReqVO).getRecords();
        ExcelUtil<GraphKnowledgeRespVO> util = new ExcelUtil<>(GraphKnowledgeRespVO.class);
        util.exportExcel(response, GraphKnowledgeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识图谱列表")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:import')")
    @Log(title = "知识图谱", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphKnowledgeRespVO> util = new ExcelUtil<>(GraphKnowledgeRespVO.class);
        List<GraphKnowledgeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphKnowledgeService.importGraphKnowledge(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识图谱详细信息")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphKnowledgeRespVO> getInfo(@PathVariable("id") Long id) {
        GraphKnowledgePO graphKnowledgePO = graphKnowledgeService.getGraphKnowledgeById(id);
        return CommonResult.success(BeanUtils.toBean(graphKnowledgePO, GraphKnowledgeRespVO.class));
    }

    @Operation(summary = "新增知识图谱")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:add')")
    @Log(title = "知识图谱", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphKnowledgeSaveReqVO graphKnowledge) {
        return CommonResult.toAjax(graphKnowledgeService.saveGraphKnowledge(graphKnowledge));
    }

    @Operation(summary = "修改知识图谱")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:edit')")
    @Log(title = "知识图谱", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphKnowledgeSaveReqVO graphKnowledge) {
        return CommonResult.toAjax(graphKnowledgeService.updateGraphKnowledge(graphKnowledge));
    }

    @Operation(summary = "删除知识图谱")
    @PreAuthorize("@ss.hasPermi('graph:manager:knowledge:remove')")
    @Log(title = "知识图谱", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphKnowledgeService.removeGraphKnowledge(Arrays.asList(ids)));
    }

}
