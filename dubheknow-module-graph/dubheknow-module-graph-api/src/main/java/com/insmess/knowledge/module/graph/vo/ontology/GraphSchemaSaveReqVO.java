package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 概念配置 创建/修改 Request VO graph_schema
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念配置 Response VO")
@Data
public class GraphSchemaSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "概念名称", example = "")
    @NotBlank(message = "概念名称不能为空")
    private String name;

    @Schema(description = "概念描述", example = "")
    @NotBlank(message = "概念描述不能为空")
    @Size(max = 1024, message = "概念描述长度不能超过1024个字符")
    private String description;

    @Schema(description = "概念颜色", example = "")
    @NotBlank(message = "概念颜色不能为空")
    @Size(max = 50, message = "概念颜色长度不能超过50个字符")
    private String color;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Schema(description = "父概念id", example = "")
    private Long pid;


}
