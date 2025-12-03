package com.insmess.knowledge.module.knowbase.vo.unstruct;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;

/**
 * 知识内容 Response VO 对象 knowbase_document_content
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识内容 Response VO")
@Data
public class KnowbaseDocumentContentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "知识文档id")
    @Schema(description = "知识文档id", example = "")
    private Long documentId;

    @Excel(name = "知识内容", readConverterExp = "解=析结果")
    @Schema(description = "知识内容", example = "")
    private String content;

}
