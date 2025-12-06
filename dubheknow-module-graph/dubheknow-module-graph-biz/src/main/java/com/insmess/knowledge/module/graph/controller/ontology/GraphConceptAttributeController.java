package com.insmess.knowledge.module.graph.controller.ontology;

import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptAttributeConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptAttributePO;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptAttributeService;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeSaveReqVO;
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
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.poi.ExcelUtil;

/**
 * 概念属性Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "概念属性")
@RestController
@RequestMapping("/graph/ontology/conceptAttribute")
@Validated
public class GraphConceptAttributeController extends BaseController {
    @Resource
    private GraphConceptAttributeService graphConceptAttributeService;

    @Operation(summary = "查询概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphConceptAttributeRespVO>> list(GraphConceptAttributePageReqVO graphConceptAttribute) {
        Page<GraphConceptAttributePO> page = graphConceptAttributeService.pageGraphConceptAttribute(graphConceptAttribute);
        return CommonResult.success(BeanUtils.toBean(page, GraphConceptAttributeRespVO.class));
    }

    @Operation(summary = "导出概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:export')")
    @Log(title = "概念属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphConceptAttributePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphConceptAttributePO> list = (List<GraphConceptAttributePO>) graphConceptAttributeService.pageGraphConceptAttribute(exportReqVO).getRecords();
        ExcelUtil<GraphConceptAttributeRespVO> util = new ExcelUtil<>(GraphConceptAttributeRespVO.class);
        util.exportExcel(response, GraphConceptAttributeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念属性列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:import')")
    @Log(title = "概念属性", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphConceptAttributeRespVO> util = new ExcelUtil<>(GraphConceptAttributeRespVO.class);
        List<GraphConceptAttributeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphConceptAttributeService.importGraphConceptAttribute(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念属性详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphConceptAttributeRespVO> getInfo(@PathVariable("id") Long id) {
        GraphConceptAttributePO graphConceptAttributePO = graphConceptAttributeService.getGraphConceptAttributeById(id);
        return CommonResult.success(BeanUtils.toBean(graphConceptAttributePO, GraphConceptAttributeRespVO.class));
    }

    @Operation(summary = "新增概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:add')")
    @Log(title = "概念属性", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphConceptAttributeSaveReqVO graphConceptAttribute) {
        return CommonResult.toAjax(graphConceptAttributeService.saveGraphConceptAttribute(graphConceptAttribute));
    }

    @Operation(summary = "修改概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:edit')")
    @Log(title = "概念属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphConceptAttributeSaveReqVO graphConceptAttribute) {
        return CommonResult.toAjax(graphConceptAttributeService.updateGraphConceptAttribute(graphConceptAttribute));
    }

    @Operation(summary = "删除概念属性")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptattribute:remove')")
    @Log(title = "概念属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphConceptAttributeService.removeGraphConceptAttribute(Arrays.asList(ids)));
    }

}
