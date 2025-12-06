package com.insmess.knowledge.module.graph.controller.extract;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskConceptMappingConvert;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskConceptMappingService;

/**
 * 概念映射Controller
 *
 * @author insmess
 * @date 2025-11-29
 */
@Tag(name = "概念映射")
@RestController
@RequestMapping("/graph/ontology/conceptMapping")
@Validated
public class GraphStructTaskConceptMappingController extends BaseController {
    @Resource
    private GraphStructTaskConceptMappingService graphStructTaskConceptMappingService;

    @Operation(summary = "查询概念映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:list')")
    @GetMapping("/list")
    public CommonResult<Page<GraphStructTaskConceptMappingRespVO>> list(GraphStructTaskConceptMappingPageReqVO graphConceptMapping) {
        Page<GraphStructTaskConceptMappingPO> page = graphStructTaskConceptMappingService.pageGraphConceptMapping(graphConceptMapping);
        return CommonResult.success(BeanUtils.toBean(page, GraphStructTaskConceptMappingRespVO.class));
    }

    @Operation(summary = "导出概念映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:export')")
    @Log(title = "概念映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraphStructTaskConceptMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GraphStructTaskConceptMappingPO> list = (List<GraphStructTaskConceptMappingPO>) graphStructTaskConceptMappingService.pageGraphConceptMapping(exportReqVO).getRecords();
        ExcelUtil<GraphStructTaskConceptMappingRespVO> util = new ExcelUtil<>(GraphStructTaskConceptMappingRespVO.class);
        util.exportExcel(response, GraphStructTaskConceptMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念映射列表")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:import')")
    @Log(title = "概念映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<GraphStructTaskConceptMappingRespVO> util = new ExcelUtil<>(GraphStructTaskConceptMappingRespVO.class);
        List<GraphStructTaskConceptMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = graphStructTaskConceptMappingService.importGraphConceptMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念映射详细信息")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<GraphStructTaskConceptMappingRespVO> getInfo(@PathVariable("id") Long id) {
        GraphStructTaskConceptMappingPO graphStructTaskConceptMappingPO = graphStructTaskConceptMappingService.getGraphConceptMappingById(id);
        return CommonResult.success(BeanUtils.toBean(graphStructTaskConceptMappingPO, GraphStructTaskConceptMappingRespVO.class));
    }

    @Operation(summary = "新增概念映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:add')")
    @Log(title = "概念映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody GraphStructTaskConceptMappingSaveReqVO graphConceptMapping) {
        return CommonResult.toAjax(graphStructTaskConceptMappingService.saveGraphConceptMapping(graphConceptMapping));
    }

    @Operation(summary = "修改概念映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:edit')")
    @Log(title = "概念映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody GraphStructTaskConceptMappingSaveReqVO graphConceptMapping) {
        return CommonResult.toAjax(graphStructTaskConceptMappingService.updateGraphConceptMapping(graphConceptMapping));
    }

    @Operation(summary = "删除概念映射")
    @PreAuthorize("@ss.hasPermi('graph:ontology:conceptmapping:remove')")
    @Log(title = "概念映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(graphStructTaskConceptMappingService.removeGraphConceptMapping(Arrays.asList(ids)));
    }

}
