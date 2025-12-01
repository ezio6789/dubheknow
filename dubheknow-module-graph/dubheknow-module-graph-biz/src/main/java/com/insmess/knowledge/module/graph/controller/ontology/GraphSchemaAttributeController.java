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
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeSaveReqVO;
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaAttributeConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaAttributePO;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaAttributeService;

/**
 * 概念属性Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "概念属性")
@RestController
@RequestMapping("/graph/ontology/schemaAttribute")
@Validated
public class GraphSchemaAttributeController extends BaseController {
    @Resource
    private GraphSchemaAttributeService graphSchemaAttributeService;

    @Operation(summary = "查询概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphSchemaAttributeRespVO>> list(GraphSchemaAttributePageReqVO graphSchemaAttribute) {
        Page<GraphSchemaAttributePO> page = graphSchemaAttributeService.pageGraphSchemaAttribute(graphSchemaAttribute);
        return CommonResult.success(BeanUtils.toBean(page, GraphSchemaAttributeRespVO.class));
    }

    @Operation(summary = "导出概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:export')")
    @Log(title = "概念属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphSchemaAttributePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphSchemaAttributePO> list = (List<GraphSchemaAttributePO>) graphSchemaAttributeService.pageGraphSchemaAttribute(exportReqVO).getRecords();
        ExcelUtil<GraphSchemaAttributeRespVO> util = new ExcelUtil<>(GraphSchemaAttributeRespVO.class);
        util.exportExcel(response, GraphSchemaAttributeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:import')")
    @Log(title = "概念属性", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphSchemaAttributeRespVO> util = new ExcelUtil<>(GraphSchemaAttributeRespVO.class);
        List<GraphSchemaAttributeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphSchemaAttributeService.importGraphSchemaAttribute(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念属性详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphSchemaAttributeRespVO> getInfo(@PathVariable("id") Long id) {
        GraphSchemaAttributePO graphSchemaAttributePO = graphSchemaAttributeService.getGraphSchemaAttributeById(id);
        return CommonResult.success(BeanUtils.toBean(graphSchemaAttributePO, GraphSchemaAttributeRespVO.class));
    }

    @Operation(summary = "新增概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:add')")
    @Log(title = "概念属性", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphSchemaAttributeSaveReqVO graphSchemaAttribute) {
        return CommonResult.toAjax(graphSchemaAttributeService.saveGraphSchemaAttribute(graphSchemaAttribute));
    }

    @Operation(summary = "修改概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:edit')")
    @Log(title = "概念属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphSchemaAttributeSaveReqVO graphSchemaAttribute) {
        return CommonResult.toAjax(graphSchemaAttributeService.updateGraphSchemaAttribute(graphSchemaAttribute));
    }

    @Operation(summary = "删除概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:schemaattribute:remove')")
    @Log(title = "概念属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphSchemaAttributeService.removeGraphSchemaAttribute(Arrays.asList(ids)));
    }

}
