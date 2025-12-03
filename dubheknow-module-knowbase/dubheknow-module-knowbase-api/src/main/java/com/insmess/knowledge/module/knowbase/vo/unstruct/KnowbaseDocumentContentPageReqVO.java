package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 知识内容 Request VO 对象 knowbase_document_content
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识内容 Request VO")
@Data
public class KnowbaseDocumentContentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "知识文档id", example = "")
    private Long documentId;

    @Schema(description = "知识内容", example = "")
    private String content;


}
