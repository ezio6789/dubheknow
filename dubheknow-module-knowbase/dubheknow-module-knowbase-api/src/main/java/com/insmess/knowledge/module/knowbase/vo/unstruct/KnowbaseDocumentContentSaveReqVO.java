package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识内容 创建/修改 Request VO knowbase_document_content
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识内容 Response VO")
@Data
public class KnowbaseDocumentContentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "知识文档id", example = "")
    private Long documentId;

    @Schema(description = "知识内容", example = "")
    @NotBlank(message = "知识内容不能为空")
    private String content;


}
