package com.insmess.knowledge.module.knowbase.controller.unstruct;

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
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentSaveReqVO;
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentConvert;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentPO;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentService;

/**
 * 知识文件Controller
 *
 * @author insmess
 * @date 2025-12-03
 */
@Tag(name = "知识文件")
@RestController
@RequestMapping("/knowbase/unstruct/knowbaseDocument")
@Validated
public class KnowbaseDocumentController extends BaseController {
    @Resource
    private KnowbaseDocumentService knowbaseDocumentService;

    @Operation(summary = "查询知识文件列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:list')")
    @GetMapping("/list")
    public CommonResult<Page<KnowbaseDocumentRespVO>> list(KnowbaseDocumentPageReqVO knowbaseDocument) {
        Page<KnowbaseDocumentPO> page = knowbaseDocumentService.pageKnowbaseDocument(knowbaseDocument);
        return CommonResult.success(BeanUtils.toBean(page, KnowbaseDocumentRespVO.class));
    }

    @Operation(summary = "导出知识文件列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:export')")
    @Log(title = "知识文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowbaseDocumentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KnowbaseDocumentPO> list = (List<KnowbaseDocumentPO>) knowbaseDocumentService.pageKnowbaseDocument(exportReqVO).getRecords();
        ExcelUtil<KnowbaseDocumentRespVO> util = new ExcelUtil<>(KnowbaseDocumentRespVO.class);
        util.exportExcel(response, KnowbaseDocumentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识文件列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:import')")
    @Log(title = "知识文件", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KnowbaseDocumentRespVO> util = new ExcelUtil<>(KnowbaseDocumentRespVO.class);
        List<KnowbaseDocumentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = knowbaseDocumentService.importKnowbaseDocument(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识文件详细信息")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KnowbaseDocumentRespVO> getInfo(@PathVariable("id") Long id) {
        KnowbaseDocumentPO knowbaseDocumentPO = knowbaseDocumentService.getKnowbaseDocumentById(id);
        return CommonResult.success(BeanUtils.toBean(knowbaseDocumentPO, KnowbaseDocumentRespVO.class));
    }

    @Operation(summary = "新增知识文件")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:add')")
    @Log(title = "知识文件", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KnowbaseDocumentSaveReqVO knowbaseDocument) {
        return CommonResult.toAjax(knowbaseDocumentService.saveKnowbaseDocument(knowbaseDocument));
    }

    @Operation(summary = "修改知识文件")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:edit')")
    @Log(title = "知识文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KnowbaseDocumentSaveReqVO knowbaseDocument) {
        return CommonResult.toAjax(knowbaseDocumentService.updateKnowbaseDocument(knowbaseDocument));
    }

    @Operation(summary = "删除知识文件")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:document:remove')")
    @Log(title = "知识文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(knowbaseDocumentService.removeKnowbaseDocument(Arrays.asList(ids)));
    }

}
