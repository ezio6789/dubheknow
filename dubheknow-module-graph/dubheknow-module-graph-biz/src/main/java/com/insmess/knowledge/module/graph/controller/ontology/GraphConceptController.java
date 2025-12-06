package com.insmess.knowledge.module.graph.controller.ontology;

import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptPageReqVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.poi.ExcelUtil;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptSaveReqVO;
import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptPO;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptService;

/**
 * 概念配置Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "概念配置")
@RestController
@RequestMapping("/graph/ontology/concept")
@Validated
public class GraphConceptController extends BaseController {
    @Resource
    private GraphConceptService graphConceptService;

    @Operation(summary = "查询概念图（本体图）")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:list')")
    @GetMapping("/getGraph")
    public CommonResult getGraph(@RequestParam("knowledgeId") Long knowledgeId) {
        Map<String, Object> graph = graphConceptService.getGraphOntology(knowledgeId);
        return CommonResult.success(graph);
    }

    @Operation(summary = "查询概念树")
    @PreAuthorize("@ss.hasPermi('ext:extConcept:concept:list')")
    @GetMapping("/tree")
    public CommonResult<List<GraphConceptRespVO>> tree(GraphConceptPageReqVO extConcept) {
        List<GraphConceptPO> list = graphConceptService.listExtConceptTree(extConcept);
        return CommonResult.success(BeanUtils.toBean(list, GraphConceptRespVO.class));
    }

    @Operation(summary = "查询概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphConceptRespVO>> list(GraphConceptPageReqVO graphConcept) {
        Page<GraphConceptPO> page = graphConceptService.pageGraphConcept(graphConcept);
        return CommonResult.success(BeanUtils.toBean(page, GraphConceptRespVO.class));
    }

    @Operation(summary = "导出概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:export')")
    @Log(title = "概念配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphConceptPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphConceptPO> list = (List<GraphConceptPO>) graphConceptService.pageGraphConcept(exportReqVO).getRecords();
        ExcelUtil<GraphConceptRespVO> util = new ExcelUtil<>(GraphConceptRespVO.class);
        util.exportExcel(response, GraphConceptConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:import')")
    @Log(title = "概念配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphConceptRespVO> util = new ExcelUtil<>(GraphConceptRespVO.class);
        List<GraphConceptRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphConceptService.importGraphConcept(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念配置详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphConceptRespVO> getInfo(@PathVariable("id") Long id) {
        GraphConceptPO graphConceptPO = graphConceptService.getGraphConceptById(id);
        return CommonResult.success(BeanUtils.toBean(graphConceptPO, GraphConceptRespVO.class));
    }

    @Operation(summary = "新增概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:add')")
    @Log(title = "概念配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphConceptSaveReqVO graphConcept) {
        return CommonResult.toAjax(graphConceptService.saveGraphConcept(graphConcept));
    }

    @Operation(summary = "修改概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:edit')")
    @Log(title = "概念配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphConceptSaveReqVO graphConcept) {
        return CommonResult.toAjax(graphConceptService.updateGraphConcept(graphConcept));
    }

    @Operation(summary = "删除概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:concept:remove')")
    @Log(title = "概念配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphConceptService.removeGraphConcept(Arrays.asList(ids)));
    }

}
