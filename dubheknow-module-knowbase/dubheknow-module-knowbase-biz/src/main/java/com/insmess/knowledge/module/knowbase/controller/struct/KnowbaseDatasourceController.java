package com.insmess.knowledge.module.knowbase.controller.struct;

import cn.hutool.json.JSONObject;
import com.insmess.knowledge.database.core.DbColumn;
import com.insmess.knowledge.database.core.DbTable;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceColumnQueryVO;
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
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourcePageReqVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceRespVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceSaveReqVO;
import com.insmess.knowledge.module.knowbase.convert.struct.KnowbaseDatasourceConvert;
import com.insmess.knowledge.module.knowbase.dao.po.struct.KnowbaseDatasourcePO;
import com.insmess.knowledge.module.knowbase.service.struct.KnowbaseDatasourceService;

/**
 * 数据源Controller
 *
 * @author insmess
 * @date 2025-12-03
 */
@Tag(name = "数据源")
@RestController
@RequestMapping("/knowbase/struct/knowbaseDatasource")
@Validated
public class KnowbaseDatasourceController extends BaseController {
    @Resource
    private KnowbaseDatasourceService knowbaseDatasourceService;

    @Operation(summary = "获取数据源里面的数据表的数据字段")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:query')")
    @PostMapping(value = "/columnsList")
    public CommonResult getColumnsList(@RequestBody KnowbaseDatasourceColumnQueryVO queryVO) {
        List<DbColumn> columns = knowbaseDatasourceService.listColumns(queryVO);
        return CommonResult.success(columns);
    }

    @Operation(summary = "获取数据源里面的数据表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:query')")
    @GetMapping(value = "/tableList/{id}")
    public CommonResult tableList(@PathVariable("id") Long id) {
        List<DbTable> tables = knowbaseDatasourceService.listTable(id);
        return CommonResult.success(tables);
    }

    @Operation(summary = "测试连接")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:list')")
    @GetMapping("/testConnection/{id}")
    public CommonResult testConnection(@PathVariable("id") Long id) {
        try {
            knowbaseDatasourceService.testConnection(id);
            return CommonResult.success(null);
        } catch (Exception e) {
            return CommonResult.error(GlobalErrorCodeConstants.ERROR);
        }
    }

    @Operation(summary = "查询数据源列表")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:list')")
    @GetMapping("/list")
    public CommonResult<Page<KnowbaseDatasourceRespVO>> list(KnowbaseDatasourcePageReqVO knowbaseDatasource) {
        Page<KnowbaseDatasourcePO> page = knowbaseDatasourceService.pageKnowbaseDatasource(knowbaseDatasource);
        return CommonResult.success(BeanUtils.toBean(page, KnowbaseDatasourceRespVO.class));
    }

    @Operation(summary = "导出数据源列表")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:export')")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowbaseDatasourcePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KnowbaseDatasourcePO> list = (List<KnowbaseDatasourcePO>) knowbaseDatasourceService.pageKnowbaseDatasource(exportReqVO).getRecords();
        ExcelUtil<KnowbaseDatasourceRespVO> util = new ExcelUtil<>(KnowbaseDatasourceRespVO.class);
        util.exportExcel(response, KnowbaseDatasourceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据源列表")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:import')")
    @Log(title = "数据源", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KnowbaseDatasourceRespVO> util = new ExcelUtil<>(KnowbaseDatasourceRespVO.class);
        List<KnowbaseDatasourceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = knowbaseDatasourceService.importKnowbaseDatasource(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据源详细信息")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KnowbaseDatasourceRespVO> getInfo(@PathVariable("id") Long id) {
        KnowbaseDatasourcePO knowbaseDatasourcePO = knowbaseDatasourceService.getKnowbaseDatasourceById(id);
        return CommonResult.success(BeanUtils.toBean(knowbaseDatasourcePO, KnowbaseDatasourceRespVO.class));
    }

    @Operation(summary = "新增数据源")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:add')")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KnowbaseDatasourceSaveReqVO knowbaseDatasource) {
        return CommonResult.toAjax(knowbaseDatasourceService.saveKnowbaseDatasource(knowbaseDatasource));
    }

    @Operation(summary = "修改数据源")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:edit')")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KnowbaseDatasourceSaveReqVO knowbaseDatasource) {
        return CommonResult.toAjax(knowbaseDatasourceService.updateKnowbaseDatasource(knowbaseDatasource));
    }

    @Operation(summary = "删除数据源")
    @PreAuthorize("@ss.hasPermi('knowbase:struct:datasource:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(knowbaseDatasourceService.removeKnowbaseDatasource(Arrays.asList(ids)));
    }

}
