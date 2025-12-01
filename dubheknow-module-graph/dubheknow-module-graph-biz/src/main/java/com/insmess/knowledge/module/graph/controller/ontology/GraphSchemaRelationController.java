package com.insmess.knowledge.module.graph.controller.ontology;

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
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationSaveReqVO;
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaRelationConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaRelationPO;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaRelationService;

/**
 * 关系配置Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "关系配置")
@RestController
@RequestMapping("/graph/ontology/schemaRelation")
@Validated
public class GraphSchemaRelationController extends BaseController {
    @Resource
    private GraphSchemaRelationService graphSchemaRelationService;

    @Operation(summary = "查询关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphSchemaRelationRespVO>> list(GraphSchemaRelationPageReqVO graphSchemaRelation) {
        Page<GraphSchemaRelationPO> page = graphSchemaRelationService.pageGraphSchemaRelation(graphSchemaRelation);
        return CommonResult.success(BeanUtils.toBean(page, GraphSchemaRelationRespVO.class));
    }

    @Operation(summary = "导出关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:export')")
    @Log(title = "关系配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphSchemaRelationPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphSchemaRelationPO> list = (List<GraphSchemaRelationPO>) graphSchemaRelationService.pageGraphSchemaRelation(exportReqVO).getRecords();
        ExcelUtil<GraphSchemaRelationRespVO> util = new ExcelUtil<>(GraphSchemaRelationRespVO.class);
        util.exportExcel(response, GraphSchemaRelationConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:import')")
    @Log(title = "关系配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphSchemaRelationRespVO> util = new ExcelUtil<>(GraphSchemaRelationRespVO.class);
        List<GraphSchemaRelationRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphSchemaRelationService.importGraphSchemaRelation(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系配置详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphSchemaRelationRespVO> getInfo(@PathVariable("id") Long id) {
        GraphSchemaRelationPO graphSchemaRelationPO = graphSchemaRelationService.getGraphSchemaRelationById(id);
        return CommonResult.success(BeanUtils.toBean(graphSchemaRelationPO, GraphSchemaRelationRespVO.class));
    }

    @Operation(summary = "新增关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:add')")
    @Log(title = "关系配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphSchemaRelationSaveReqVO graphSchemaRelation) {
        return CommonResult.toAjax(graphSchemaRelationService.saveGraphSchemaRelation(graphSchemaRelation));
    }

    @Operation(summary = "修改关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:edit')")
    @Log(title = "关系配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphSchemaRelationSaveReqVO graphSchemaRelation) {
        return CommonResult.toAjax(graphSchemaRelationService.updateGraphSchemaRelation(graphSchemaRelation));
    }

    @Operation(summary = "删除关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemarelation:remove')")
    @Log(title = "关系配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphSchemaRelationService.removeGraphSchemaRelation(Arrays.asList(ids)));
    }

}
