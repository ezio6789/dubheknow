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
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentSaveReqVO;
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentContentConvert;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentContentPO;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentContentService;

/**
 * 知识内容Controller
 *
 * @author insmess
 * @date 2025-12-03
 */
@Tag(name = "知识内容")
@RestController
@RequestMapping("/knowbase/unstruct/knowbaseDocumentContent")
@Validated
public class KnowbaseDocumentContentController extends BaseController {
    @Resource
    private KnowbaseDocumentContentService knowbaseDocumentContentService;

    @Operation(summary = "查询知识内容列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:list')")
    @GetMapping("/list")
    public CommonResult<Page<KnowbaseDocumentContentRespVO>> list(KnowbaseDocumentContentPageReqVO knowbaseDocumentContent) {
        Page<KnowbaseDocumentContentPO> page = knowbaseDocumentContentService.pageKnowbaseDocumentContent(knowbaseDocumentContent);
        return CommonResult.success(BeanUtils.toBean(page, KnowbaseDocumentContentRespVO.class));
    }

    @Operation(summary = "导出知识内容列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:export')")
    @Log(title = "知识内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowbaseDocumentContentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KnowbaseDocumentContentPO> list = (List<KnowbaseDocumentContentPO>) knowbaseDocumentContentService.pageKnowbaseDocumentContent(exportReqVO).getRecords();
        ExcelUtil<KnowbaseDocumentContentRespVO> util = new ExcelUtil<>(KnowbaseDocumentContentRespVO.class);
        util.exportExcel(response, KnowbaseDocumentContentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识内容列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:import')")
    @Log(title = "知识内容", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KnowbaseDocumentContentRespVO> util = new ExcelUtil<>(KnowbaseDocumentContentRespVO.class);
        List<KnowbaseDocumentContentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = knowbaseDocumentContentService.importKnowbaseDocumentContent(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识内容详细信息")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KnowbaseDocumentContentRespVO> getInfo(@PathVariable("id") Long id) {
        KnowbaseDocumentContentPO knowbaseDocumentContentPO = knowbaseDocumentContentService.getKnowbaseDocumentContentById(id);
        return CommonResult.success(BeanUtils.toBean(knowbaseDocumentContentPO, KnowbaseDocumentContentRespVO.class));
    }

    @Operation(summary = "新增知识内容")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:add')")
    @Log(title = "知识内容", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KnowbaseDocumentContentSaveReqVO knowbaseDocumentContent) {
        return CommonResult.toAjax(knowbaseDocumentContentService.saveKnowbaseDocumentContent(knowbaseDocumentContent));
    }

    @Operation(summary = "修改知识内容")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:edit')")
    @Log(title = "知识内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KnowbaseDocumentContentSaveReqVO knowbaseDocumentContent) {
        return CommonResult.toAjax(knowbaseDocumentContentService.updateKnowbaseDocumentContent(knowbaseDocumentContent));
    }

    @Operation(summary = "删除知识内容")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentcontent:remove')")
    @Log(title = "知识内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(knowbaseDocumentContentService.removeKnowbaseDocumentContent(Arrays.asList(ids)));
    }

}
