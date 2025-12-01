package com.insmess.knowledge.module.graph.controller.ontology;

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
import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.poi.ExcelUtil;
import com.insmess.knowledge.common.exception.enums.GlobalErrorCodeConstants;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaSaveReqVO;
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaPO;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaService;

/**
 * 概念配置Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "概念配置")
@RestController
@RequestMapping("/graph/ontology/schema")
@Validated
public class GraphSchemaController extends BaseController {
    @Resource
    private GraphSchemaService graphSchemaService;

    @Operation(summary = "查询概念图（本体图）")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:list')")
    @GetMapping("/getGraph")
    public CommonResult getGraph(@RequestParam("knowledgeId") Long knowledgeId) {
        Map<String, Object> graph = graphSchemaService.getGraphOntology(knowledgeId);
        return CommonResult.success(graph);
    }

    @Operation(summary = "查询概念树")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:list')")
    @GetMapping("/tree")
    public CommonResult<List<GraphSchemaRespVO>> tree(GraphSchemaPageReqVO extSchema) {
        List<GraphSchemaPO> list = graphSchemaService.listExtSchemaTree(extSchema);
        return CommonResult.success(BeanUtils.toBean(list, GraphSchemaRespVO.class));
    }

    @Operation(summary = "查询概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphSchemaRespVO>> list(GraphSchemaPageReqVO graphSchema) {
        Page<GraphSchemaPO> page = graphSchemaService.pageGraphSchema(graphSchema);
        return CommonResult.success(BeanUtils.toBean(page, GraphSchemaRespVO.class));
    }

    @Operation(summary = "导出概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:export')")
    @Log(title = "概念配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphSchemaPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphSchemaPO> list = (List<GraphSchemaPO>) graphSchemaService.pageGraphSchema(exportReqVO).getRecords();
        ExcelUtil<GraphSchemaRespVO> util = new ExcelUtil<>(GraphSchemaRespVO.class);
        util.exportExcel(response, GraphSchemaConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:import')")
    @Log(title = "概念配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphSchemaRespVO> util = new ExcelUtil<>(GraphSchemaRespVO.class);
        List<GraphSchemaRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphSchemaService.importGraphSchema(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念配置详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphSchemaRespVO> getInfo(@PathVariable("id") Long id) {
        GraphSchemaPO graphSchemaPO = graphSchemaService.getGraphSchemaById(id);
        return CommonResult.success(BeanUtils.toBean(graphSchemaPO, GraphSchemaRespVO.class));
    }

    @Operation(summary = "新增概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:add')")
    @Log(title = "概念配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphSchemaSaveReqVO graphSchema) {
        return CommonResult.toAjax(graphSchemaService.saveGraphSchema(graphSchema));
    }

    @Operation(summary = "修改概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:edit')")
    @Log(title = "概念配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphSchemaSaveReqVO graphSchema) {
        return CommonResult.toAjax(graphSchemaService.updateGraphSchema(graphSchema));
    }

    @Operation(summary = "删除概念配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schema:remove')")
    @Log(title = "概念配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphSchemaService.removeGraphSchema(Arrays.asList(ids)));
    }

}
