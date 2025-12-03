package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识目录 创建/修改 Request VO knowbase_document_dir
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识目录 Response VO")
@Data
public class KnowbaseDocumentDirSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "父级id", example = "")
    @NotNull(message = "父级id不能为空")
    private Long parentId;

    @Schema(description = "分类名称", example = "")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "显示顺序", example = "")
    private Long orderNum;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
