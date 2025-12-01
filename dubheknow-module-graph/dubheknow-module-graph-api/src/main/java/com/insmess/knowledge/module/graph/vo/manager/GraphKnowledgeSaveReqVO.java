package com.insmess.knowledge.module.graph.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识图谱 创建/修改 Request VO graph_knowledge
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "知识图谱 Response VO")
@Data
public class GraphKnowledgeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "知识图谱名称", example = "")
    @NotBlank(message = "知识图谱名称不能为空")
    @Size(max = 50, message = "知识图谱名称长度不能超过50个字符")
    private String name;

    @Schema(description = "知识图谱描述", example = "")
    @NotBlank(message = "知识图谱描述不能为空")
    private String description;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
