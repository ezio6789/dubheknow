package com.insmess.knowledge.module.graph.controller.extract;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskAttributeMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskAttributeMappingConvert;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskAttributeMappingService;

/**
 * 属性映射Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "属性映射")
@RestController
@RequestMapping("/graph/ontology/attributeMapping")
@Validated
public class GraphStructTaskAttributeMappingController extends BaseController {
    @Resource
    private GraphStructTaskAttributeMappingService graphStructTaskAttributeMappingService;

    @Operation(summary = "查询属性映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphStructTaskAttributeMappingRespVO>> list(GraphStructTaskAttributeMappingPageReqVO graphAttributeMapping) {
        Page<GraphStructTaskAttributeMappingPO> page = graphStructTaskAttributeMappingService.pageGraphAttributeMapping(graphAttributeMapping);
        return CommonResult.success(BeanUtils.toBean(page, GraphStructTaskAttributeMappingRespVO.class));
    }

    @Operation(summary = "导出属性映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:export')")
    @Log(title = "属性映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphStructTaskAttributeMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphStructTaskAttributeMappingPO> list = (List<GraphStructTaskAttributeMappingPO>) graphStructTaskAttributeMappingService.pageGraphAttributeMapping(exportReqVO).getRecords();
        ExcelUtil<GraphStructTaskAttributeMappingRespVO> util = new ExcelUtil<>(GraphStructTaskAttributeMappingRespVO.class);
        util.exportExcel(response, GraphStructTaskAttributeMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入属性映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:import')")
    @Log(title = "属性映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphStructTaskAttributeMappingRespVO> util = new ExcelUtil<>(GraphStructTaskAttributeMappingRespVO.class);
        List<GraphStructTaskAttributeMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphStructTaskAttributeMappingService.importGraphAttributeMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取属性映射详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphStructTaskAttributeMappingRespVO> getInfo(@PathVariable("id") Long id) {
        GraphStructTaskAttributeMappingPO graphStructTaskAttributeMappingPO = graphStructTaskAttributeMappingService.getGraphAttributeMappingById(id);
        return CommonResult.success(BeanUtils.toBean(graphStructTaskAttributeMappingPO, GraphStructTaskAttributeMappingRespVO.class));
    }

    @Operation(summary = "新增属性映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:add')")
    @Log(title = "属性映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphStructTaskAttributeMappingSaveReqVO graphAttributeMapping) {
        return CommonResult.toAjax(graphStructTaskAttributeMappingService.saveGraphAttributeMapping(graphAttributeMapping));
    }

    @Operation(summary = "修改属性映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:edit')")
    @Log(title = "属性映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphStructTaskAttributeMappingSaveReqVO graphAttributeMapping) {
        return CommonResult.toAjax(graphStructTaskAttributeMappingService.updateGraphAttributeMapping(graphAttributeMapping));
    }

    @Operation(summary = "删除属性映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:attributemapping:remove')")
    @Log(title = "属性映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphStructTaskAttributeMappingService.removeGraphAttributeMapping(Arrays.asList(ids)));
    }

}
