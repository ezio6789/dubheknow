package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 知识文件 Request VO 对象 knowbase_document
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识文件 Request VO")
@Data
public class KnowbaseDocumentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "目录id", example = "")
    private Long dirId;

    @Schema(description = "目录名称", example = "")
    private String dirName;

    @Schema(description = "文件名称", example = "")
    private String name;

    @Schema(description = "文件类型", example = "")
    private String type;

    @Schema(description = "解析状态", example = "")
    private Integer parseStatus;

    @Schema(description = "文件url", example = "")
    private String url;

    @Schema(description = "文件描述", example = "")
    private String description;




}
