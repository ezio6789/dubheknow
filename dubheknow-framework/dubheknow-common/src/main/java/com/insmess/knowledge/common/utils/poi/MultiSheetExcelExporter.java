package com.insmess.knowledge.common.utils.poi;

import com.insmess.knowledge.common.annotation.Excel;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多Sheet Excel导出工具类
 * 支持导出多个不同实体类的数据到不同sheet页
 */
public class MultiSheetExcelExporter {

    private final Workbook workbook;
    private final List<SheetConfig> sheetConfigs = new ArrayList<>();
    // 用于导入时存储sheet页与实体类的映射关系
    private final Map<String, Class<?>> sheetClassMap = new HashMap<>();
    // 移除final修饰，仅在导入时使用，导出场景可为null
    private InputStream inputStream;
    // 新增：存储文件字节数组，解决流复用问题（核心修正）
    private byte[] fileBytes;

    /**
     * Sheet配置内部类
     */
    private static class SheetConfig<T> {
        ExcelUtil<T> excelUtil;

        SheetConfig(ExcelUtil<T> excelUtil) {
            this.excelUtil = excelUtil;
        }
    }

    /**
     * 初始化多sheet导出器（仅用于导出）
     *
     * @param isXlsx 是否为xlsx格式（true为xlsx，false为xls）
     */
    public MultiSheetExcelExporter(boolean isXlsx) {
        // 导出场景：不需要inputStream，仅初始化workbook
        this.workbook = isXlsx ? new org.apache.poi.xssf.usermodel.XSSFWorkbook() : new org.apache.poi.hssf.usermodel.HSSFWorkbook();
        this.inputStream = null;
        this.fileBytes = null;
    }

    /**
     * 构造方法：接收输入流用于导入
     *
     * @param inputStream 输入流
     * @throws IOException IO异常
     */
    public MultiSheetExcelExporter(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        // 读取流内容到字节数组，方便后续复用
        this.fileBytes = IOUtils.toByteArray(inputStream);
        this.workbook = WorkbookFactory.create(new ByteArrayInputStream(fileBytes));
    }

    /**
     * 初始化多sheet处理器（用于导入）
     *
     * @param file 上传的Excel文件
     * @throws IOException IO异常
     */
    public MultiSheetExcelExporter(MultipartFile file) throws IOException {
        // 从MultipartFile获取字节数组
        this.fileBytes = file.getBytes();
        this.inputStream = new ByteArrayInputStream(fileBytes);
        this.workbook = WorkbookFactory.create(this.inputStream);
    }

    /**
     * 添加一个模板sheet页配置（无数据，用于导出模板）
     *
     * @param sheetName     sheet名称
     * @param title         标题
     * @param clazz         实体类
     * @param <T>           实体类型
     */
    public <T> void addTemplateSheet(String sheetName, String title, Class<T> clazz) {
        addTemplateSheet(sheetName, title, clazz, null);
    }

    /**
     * 添加一个模板sheet页配置（带排除字段，无数据，用于导出模板）
     *
     * @param sheetName     sheet名称
     * @param title         标题
     * @param clazz         实体类
     * @param excludeFields 需要排除的字段
     * @param <T>           实体类型
     */
    public <T> void addTemplateSheet(String sheetName, String title, Class<T> clazz, String[] excludeFields) {
        ExcelUtil<T> excelUtil = new ExcelUtil<>(clazz);
        if (excludeFields != null && excludeFields.length > 0) {
            excelUtil.hideColumn(excludeFields);
        }
        // 初始化ExcelUtil，使用空数据列表作为模板
        excelUtil.init(new ArrayList<>(), sheetName, title, Excel.Type.EXPORT);
        // 关联共享的workbook
        excelUtil.wb = this.workbook;
        // 创建sheet并关联到当前workbook
        excelUtil.sheet = workbook.createSheet(sheetName);
        // 重新初始化样式（基于共享workbook）
        excelUtil.styles = excelUtil.createStyles(workbook);
        // 添加到sheet配置列表
        sheetConfigs.add(new SheetConfig<>(excelUtil));
    }


    /**
     * 添加一个sheet页配置
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param data      数据列表
     * @param clazz     实体类
     * @param <T>       实体类型
     */
    public <T> void addSheet(String sheetName, String title, List<T> data, Class<T> clazz) {
        addSheet(sheetName, title, data, clazz, null);
    }

    /**
     * 添加一个sheet页配置（带排除字段）
     *
     * @param sheetName     sheet名称
     * @param title         标题
     * @param data          数据列表
     * @param clazz         实体类
     * @param excludeFields 需要排除的字段
     * @param <T>           实体类型
     */
    public <T> void addSheet(String sheetName, String title, List<T> data, Class<T> clazz, String[] excludeFields) {
        ExcelUtil<T> excelUtil = new ExcelUtil<>(clazz);
        if (excludeFields != null && excludeFields.length > 0) {
            excelUtil.hideColumn(excludeFields);
        }
        // 初始化ExcelUtil但不创建新工作簿
        excelUtil.init(data, sheetName, title, Excel.Type.EXPORT);
        // 替换为共享的workbook
        excelUtil.wb = this.workbook;
        // 创建sheet并关联到当前workbook
        excelUtil.sheet = workbook.createSheet(sheetName);
        // 重新初始化样式（基于共享workbook）
        excelUtil.styles = excelUtil.createStyles(workbook);
        sheetConfigs.add(new SheetConfig<>(excelUtil));
    }

    /**
     * 通过反射创建ExcelUtil实例
     */
    @SuppressWarnings("unchecked")
    private <T> ExcelUtil<T> createExcelUtil(Class<T> clazz) throws Exception {
        return (ExcelUtil<T>) ExcelUtil.class.getConstructor(Class.class).newInstance(clazz);
    }

    /**
     * 执行导出到响应流
     *
     * @param response Http响应对象
     * @param fileName 文件名
     * @throws IOException IO异常
     */
    public void export(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        try (OutputStream os = response.getOutputStream()) {
            writeSheets();
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    /**
     * 写入所有sheet数据
     */
    private void writeSheets() {
        for (SheetConfig<?> config : sheetConfigs) {
            ExcelUtil<?> excelUtil = config.excelUtil;
            // 写入当前sheet数据
            excelUtil.writeSheet();
        }
    }

    // 导入方法修正（使用fileBytes创建新流，避免流关闭问题）
    public Map<String, List<?>> importTemplateSheets(int titleNum) {
        Map<String, List<?>> result = new HashMap<>();
        if (fileBytes == null) {
            throw new IllegalStateException("未初始化导入文件，请使用带InputStream或MultipartFile的构造方法");
        }

        for (Map.Entry<String, Class<?>> entry : sheetClassMap.entrySet()) {
            String sheetName = entry.getKey();
            Class<?> clazz = entry.getValue();
            try (InputStream is = new ByteArrayInputStream(fileBytes)) { // 每次创建新流
                ExcelUtil<?> excelUtil = new ExcelUtil<>(clazz);
                // 调用ExcelUtil的导入方法（支持指定sheet名称和标题行）
                List<?> dataList = excelUtil.importExcel(sheetName, is, titleNum);
                result.put(sheetName, dataList);
            } catch (Exception e) {
                throw new RuntimeException("导入sheet[" + sheetName + "]数据失败", e);
            }
        }
        return result;
    }

    /**
     * 导入多个sheet的模板数据（默认标题行占1行）
     */
    public Map<String, List<?>> importTemplateSheets() {
        return importTemplateSheets(0);
    }

    /**
     * 重置输入流（解决流被多次读取的问题）
     */
    private void resetInputStream() {
        try {
            if (inputStream.markSupported()) {
                inputStream.reset();
            } else {
                throw new RuntimeException("输入流不支持重置，请使用可标记的输入流（如BufferedInputStream）");
            }
        } catch (IOException e) {
            throw new RuntimeException("重置输入流失败", e);
        }
    }

    // 注册方法保持不变
    public <T> void registerImportSheet(String sheetName, Class<T> clazz) {
        sheetClassMap.put(sheetName, clazz);
    }

    public <T> void registerImportSheet(String sheetName, Class<T> clazz, String[] excludeFields) {
        sheetClassMap.put(sheetName, clazz);
        // 处理排除字段逻辑（如果需要）
    }
}