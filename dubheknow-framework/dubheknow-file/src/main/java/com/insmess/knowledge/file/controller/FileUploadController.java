package com.insmess.knowledge.file.controller;

import com.insmess.knowledge.common.core.domain.CommonResult;
import com.insmess.knowledge.file.storage.properties.StorageProperties;
import com.insmess.knowledge.file.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;


/**
 * 文件上传控制器
 * 提供一系列文件上传的 API 接口
 * 该控制器将调用静态工具类 FileUploadUtil 来实现文件上传功能
 * 使用 @RestController 注解以支持 RESTful API 形式
 *
 * @author insmess
 */
@Tag(name = "文件系统")
@RestController
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageProperties storageProperties;

    @PostConstruct
    public void init() {
    }

    @SneakyThrows
    @GetMapping("/file/{key}")
    public Object file(@PathVariable("key") String key) {
        if (key == null) {
            return CommonResult.error(400, "缺少核心参数");
        }
        if (key.contains("../")) {
            return CommonResult.error(400, "非法访问");
        }
        //获取路径
        //获取文件
        String prefix = storageProperties.getLocal().getPath();
        File file = new File(prefix, key);
        //获取文件媒体类型
        Tika tika = new Tika();
        String mediaType = tika.detect(file);
        Resource resource = new UrlResource(file.toURI());
        //返回file的文件流
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header("Content-Disposition", "attachment;filename=" + file.getName())
                .body(resource);
    }

    /**
     * 上传文件接口 -可用
     * 处理文件上传请求，将文件上传到默认存储平台
     *
     * @param file 要上传的文件，使用 MultipartFile 接收上传的文件
     * @return 上传成功后返回文件信息（FileInfo 对象）
     */
    @Operation(summary = "文件上传", description = "文件上传")
    @SneakyThrows
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileInfo upload(@RequestParam("file") MultipartFile file) {
        InputStream in = file.getInputStream();
        String path = storageService.getPath(file.getOriginalFilename());
        String url = storageService.upload(in, path);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        fileInfo.setFilename(file.getOriginalFilename());
        return fileInfo;
    }

    /**
     * 上传图片接口 -暂不可用
     * 处理图片文件上传请求，同时生成缩略图
     *
     * @param file 要上传的图片文件，使用 MultipartFile 接收上传的图片文件
     * @return 上传成功后返回图片文件的信息（FileInfo 对象）
     */
    @SneakyThrows
    @PostMapping("/upload-image")
    public FileInfo uploadImage(@RequestParam("file")MultipartFile file) {
        InputStream in = file.getInputStream();
        String path = storageService.getPath(file.getName());
        String url = storageService.upload(in, path);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        fileInfo.setFilename(file.getOriginalFilename());
        return fileInfo;
    }

    /**
     * 上传文件到指定存储平台的接口 -暂不可用
     * 处理文件上传请求，将文件上传到指定的存储平台
     *
     * @param file 要上传的文件，使用 MultipartFile 接收上传的文件
     * @return 上传成功后返回文件信息（FileInfo 对象）
     */
    @SneakyThrows
    @PostMapping("/upload-platform")
    public FileInfo uploadPlatform(@RequestParam("file")MultipartFile file) {
        InputStream in = file.getInputStream();
        String path = storageService.getPath(file.getName());
        String url = storageService.upload(in, path);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        fileInfo.setFilename(file.getOriginalFilename());
        return fileInfo;
    }

}
