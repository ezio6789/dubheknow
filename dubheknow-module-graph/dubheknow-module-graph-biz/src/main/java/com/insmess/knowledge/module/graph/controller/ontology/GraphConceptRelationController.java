package com.insmess.knowledge.module.graph.controller.ontology;

import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptRelationConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptRelationPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationRespVO;
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
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationSaveReqVO;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptRelationService;

/**
 * 关系配置Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "关系配置")
@RestController
@RequestMapping("/graph/ontology/conceptRelation")
@Validated
public class GraphConceptRelationController extends BaseController {
    @Resource
    private GraphConceptRelationService graphConceptRelationService;

    @Operation(summary = "查询关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphConceptRelationRespVO>> list(GraphConceptRelationPageReqVO graphConceptRelation) {
        Page<GraphConceptRelationPO> page = graphConceptRelationService.pageGraphConceptRelation(graphConceptRelation);
        return CommonResult.success(BeanUtils.toBean(page, GraphConceptRelationRespVO.class));
    }

    @Operation(summary = "导出关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:export')")
    @Log(title = "关系配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphConceptRelationPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphConceptRelationPO> list = (List<GraphConceptRelationPO>) graphConceptRelationService.pageGraphConceptRelation(exportReqVO).getRecords();
        ExcelUtil<GraphConceptRelationRespVO> util = new ExcelUtil<>(GraphConceptRelationRespVO.class);
        util.exportExcel(response, GraphConceptRelationConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系配置列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:import')")
    @Log(title = "关系配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphConceptRelationRespVO> util = new ExcelUtil<>(GraphConceptRelationRespVO.class);
        List<GraphConceptRelationRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphConceptRelationService.importGraphConceptRelation(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系配置详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphConceptRelationRespVO> getInfo(@PathVariable("id") Long id) {
        GraphConceptRelationPO graphConceptRelationPO = graphConceptRelationService.getGraphConceptRelationById(id);
        return CommonResult.success(BeanUtils.toBean(graphConceptRelationPO, GraphConceptRelationRespVO.class));
    }

    @Operation(summary = "新增关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:add')")
    @Log(title = "关系配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphConceptRelationSaveReqVO graphConceptRelation) {
        return CommonResult.toAjax(graphConceptRelationService.saveGraphConceptRelation(graphConceptRelation));
    }

    @Operation(summary = "修改关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:edit')")
    @Log(title = "关系配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphConceptRelationSaveReqVO graphConceptRelation) {
        return CommonResult.toAjax(graphConceptRelationService.updateGraphConceptRelation(graphConceptRelation));
    }

    @Operation(summary = "删除关系配置")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptrelation:remove')")
    @Log(title = "关系配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphConceptRelationService.removeGraphConceptRelation(Arrays.asList(ids)));
    }

}
