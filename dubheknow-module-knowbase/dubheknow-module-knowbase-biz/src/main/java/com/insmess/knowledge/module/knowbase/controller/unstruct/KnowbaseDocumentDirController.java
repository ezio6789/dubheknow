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
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirSaveReqVO;
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentDirConvert;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentDirPO;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentDirService;

/**
 * 知识目录Controller
 *
 * @author insmess
 * @date 2025-12-03
 */
@Tag(name = "知识目录")
@RestController
@RequestMapping("/knowbase/unstruct/knowbaseDocumentDir")
@Validated
public class KnowbaseDocumentDirController extends BaseController {
    @Resource
    private KnowbaseDocumentDirService knowbaseDocumentDirService;

    @Operation(summary = "查询知识目录列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:list')")
    @GetMapping("/list")
    public CommonResult<Page<KnowbaseDocumentDirRespVO>> list(KnowbaseDocumentDirPageReqVO knowbaseDocumentDir) {
        Page<KnowbaseDocumentDirPO> page = knowbaseDocumentDirService.pageKnowbaseDocumentDir(knowbaseDocumentDir);
        return CommonResult.success(BeanUtils.toBean(page, KnowbaseDocumentDirRespVO.class));
    }

    @Operation(summary = "导出知识目录列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:export')")
    @Log(title = "知识目录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowbaseDocumentDirPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KnowbaseDocumentDirPO> list = (List<KnowbaseDocumentDirPO>) knowbaseDocumentDirService.pageKnowbaseDocumentDir(exportReqVO).getRecords();
        ExcelUtil<KnowbaseDocumentDirRespVO> util = new ExcelUtil<>(KnowbaseDocumentDirRespVO.class);
        util.exportExcel(response, KnowbaseDocumentDirConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识目录列表")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:import')")
    @Log(title = "知识目录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KnowbaseDocumentDirRespVO> util = new ExcelUtil<>(KnowbaseDocumentDirRespVO.class);
        List<KnowbaseDocumentDirRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = knowbaseDocumentDirService.importKnowbaseDocumentDir(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识目录详细信息")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KnowbaseDocumentDirRespVO> getInfo(@PathVariable("id") Long id) {
        KnowbaseDocumentDirPO knowbaseDocumentDirPO = knowbaseDocumentDirService.getKnowbaseDocumentDirById(id);
        return CommonResult.success(BeanUtils.toBean(knowbaseDocumentDirPO, KnowbaseDocumentDirRespVO.class));
    }

    @Operation(summary = "新增知识目录")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:add')")
    @Log(title = "知识目录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KnowbaseDocumentDirSaveReqVO knowbaseDocumentDir) {
        return CommonResult.toAjax(knowbaseDocumentDirService.saveKnowbaseDocumentDir(knowbaseDocumentDir));
    }

    @Operation(summary = "修改知识目录")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:edit')")
    @Log(title = "知识目录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KnowbaseDocumentDirSaveReqVO knowbaseDocumentDir) {
        return CommonResult.toAjax(knowbaseDocumentDirService.updateKnowbaseDocumentDir(knowbaseDocumentDir));
    }

    @Operation(summary = "删除知识目录")
    @PreAuthorize("@ss.hasPermi('knowbase:unstruct:documentdir:remove')")
    @Log(title = "知识目录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(knowbaseDocumentDirService.removeKnowbaseDocumentDir(Arrays.asList(ids)));
    }

}
