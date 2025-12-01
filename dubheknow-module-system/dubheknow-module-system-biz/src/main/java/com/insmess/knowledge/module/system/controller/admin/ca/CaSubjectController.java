package com.insmess.knowledge.module.system.controller.admin.ca;

import com.insmess.knowledge.common.annotation.Log;
import com.insmess.knowledge.common.core.controller.BaseController;
import com.insmess.knowledge.common.core.domain.AjaxResult;
import com.insmess.knowledge.module.system.ca.domain.CaSubject;
import com.insmess.knowledge.module.system.ca.service.ICaSubjectService;
import com.insmess.knowledge.common.core.page.TableDataInfo;
import com.insmess.knowledge.common.enums.BusinessType;
import com.insmess.knowledge.config.ServerConfig;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 主体管理Controller
 *
 * @author insmess
 * @date 2024-08-18
 */
@RestController
@RequestMapping("/ca/subject")
public class CaSubjectController extends BaseController
{
    @Autowired
    private ICaSubjectService caSubjectService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ServerConfig serverConfig;

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    @PostConstruct
    public void init() {
//        FileUploadUtil.init(fileStorageService, serverConfig, storagePath);
    }
    /**
     * 查询主体管理列表
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(CaSubject caSubject)
    {
        startPage();
        List<CaSubject> list = caSubjectService.selectCaSubjectList(caSubject);
        return getDataTable(list);
    }

    /**
     * 导出主体管理列表
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:export')")
    @Log(title = "主体管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CaSubject caSubject)
    {
        throw new RuntimeException("暂不支持");
//        List<CaSubject> list = caSubjectService.selectCaSubjectList(caSubject);
//        ExcelUtil<CaSubject> util = new ExcelUtil<CaSubject>(CaSubject.class);
//        util.exportExcel(response, list, "主体管理数据");
    }

    /**
     * 获取主体管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(caSubjectService.selectCaSubjectById(id));
    }

    /**
     * 新增主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:add')")
    @Log(title = "主体管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CaSubject caSubject)
    {
        throw new RuntimeException("暂不支持");
//        // 构建证书的 DN 名称
//        String dnNameStr = StringUtils.format("CN={}, OU={}, O={}, L={}, ST={}, C={}",
//                caSubject.getCn(), caSubject.getOu(),
//                caSubject.getO(), caSubject.getL(),
//                caSubject.getSt(), caSubject.getC());
//
//        // 生成并获取根证书和私钥的文件列表
//        List<MultipartFile> fileList = CaGenerateRootCertificate.generateRootCertificate(dnNameStr);
//
//        // 上传并获取证书和私钥的文件信息
//        FileInfo cert = FileUploadUtil.upload(fileList.get(0), "ca/");
//        FileInfo privateKey = FileUploadUtil.upload(fileList.get(1), "ca/");
//
//        // 更新数据信息
//        caSubject.setCertificate(Constants.RESOURCE_PREFIX + "/" + cert.getPath() + cert.getFilename());
//        caSubject.setPrivateKey(Constants.RESOURCE_PREFIX + "/" + privateKey.getPath() + privateKey.getFilename());
//        caSubject.setCreatorId(getUserId());
//        caSubject.setCreateBy(getNickName());
//        return toAjax(caSubjectService.insertCaSubject(caSubject));
    }

    /**
     * 修改主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:edit')")
    @Log(title = "主体管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CaSubject caSubject)
    {
        return toAjax(caSubjectService.updateCaSubject(caSubject));
    }

    /**
     * 删除主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:remove')")
    @Log(title = "主体管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        return toAjax(caSubjectService.deleteCaSubjectByIds(ids));
    }
}
