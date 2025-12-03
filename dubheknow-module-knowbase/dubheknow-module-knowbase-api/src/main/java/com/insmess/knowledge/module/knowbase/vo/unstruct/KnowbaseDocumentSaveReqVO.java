package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识文件 创建/修改 Request VO knowbase_document
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KnowbaseDocumentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "目录id", example = "")
    @NotNull(message = "目录id不能为空")
    private Long dirId;

    @Schema(description = "目录名称", example = "")
    @NotBlank(message = "目录名称不能为空")
    @Size(max = 128, message = "目录名称长度不能超过128个字符")
    private String dirName;

    @Schema(description = "文件名称", example = "")
    @NotBlank(message = "文件名称不能为空")
    private String name;

    @Schema(description = "文件类型", example = "")
    @Size(max = 255, message = "文件类型长度不能超过255个字符")
    private String type;

    @Schema(description = "解析状态", example = "")
    private Integer parseStatus;

    @Schema(description = "文件url", example = "")
    @NotBlank(message = "文件url不能为空")
    private String url;

    @Schema(description = "文件描述", example = "")
    @NotBlank(message = "文件描述不能为空")
    @Size(max = 1024, message = "文件描述长度不能超过1024个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
