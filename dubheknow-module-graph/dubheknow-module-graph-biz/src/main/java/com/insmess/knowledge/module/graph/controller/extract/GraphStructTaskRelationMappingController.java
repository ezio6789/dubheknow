package com.insmess.knowledge.module.graph.controller.extract;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskRelationMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskRelationMappingConvert;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskRelationMappingService;

/**
 * 关系映射Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "关系映射")
@RestController
@RequestMapping("/graph/ontology/relationMapping")
@Validated
public class GraphStructTaskRelationMappingController extends BaseController {
    @Resource
    private GraphStructTaskRelationMappingService graphStructTaskRelationMappingService;

    @Operation(summary = "查询关系映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphStructTaskRelationMappingRespVO>> list(GraphStructTaskRelationMappingPageReqVO graphRelationMapping) {
        Page<GraphStructTaskRelationMappingPO> page = graphStructTaskRelationMappingService.pageGraphRelationMapping(graphRelationMapping);
        return CommonResult.success(BeanUtils.toBean(page, GraphStructTaskRelationMappingRespVO.class));
    }

    @Operation(summary = "导出关系映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:export')")
    @Log(title = "关系映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphStructTaskRelationMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphStructTaskRelationMappingPO> list = (List<GraphStructTaskRelationMappingPO>) graphStructTaskRelationMappingService.pageGraphRelationMapping(exportReqVO).getRecords();
        ExcelUtil<GraphStructTaskRelationMappingRespVO> util = new ExcelUtil<>(GraphStructTaskRelationMappingRespVO.class);
        util.exportExcel(response, GraphStructTaskRelationMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:import')")
    @Log(title = "关系映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphStructTaskRelationMappingRespVO> util = new ExcelUtil<>(GraphStructTaskRelationMappingRespVO.class);
        List<GraphStructTaskRelationMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphStructTaskRelationMappingService.importGraphRelationMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系映射详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphStructTaskRelationMappingRespVO> getInfo(@PathVariable("id") Long id) {
        GraphStructTaskRelationMappingPO graphStructTaskRelationMappingPO = graphStructTaskRelationMappingService.getGraphRelationMappingById(id);
        return CommonResult.success(BeanUtils.toBean(graphStructTaskRelationMappingPO, GraphStructTaskRelationMappingRespVO.class));
    }

    @Operation(summary = "新增关系映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:add')")
    @Log(title = "关系映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphStructTaskRelationMappingSaveReqVO graphRelationMapping) {
        return CommonResult.toAjax(graphStructTaskRelationMappingService.saveGraphRelationMapping(graphRelationMapping));
    }

    @Operation(summary = "修改关系映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:edit')")
    @Log(title = "关系映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphStructTaskRelationMappingSaveReqVO graphRelationMapping) {
        return CommonResult.toAjax(graphStructTaskRelationMappingService.updateGraphRelationMapping(graphRelationMapping));
    }

    @Operation(summary = "删除关系映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:relationmapping:remove')")
    @Log(title = "关系映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphStructTaskRelationMappingService.removeGraphRelationMapping(Arrays.asList(ids)));
    }

}
