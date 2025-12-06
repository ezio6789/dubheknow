package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 关系配置 创建/修改 Request VO graph_concept_relation
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "关系配置 Response VO")
@Data
public class GraphConceptRelationSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "起点概念id", example = "")
    @NotNull(message = "起点概念id不能为空")
    private Long startConceptId;

    @Schema(description = "关系", example = "")
    @NotBlank(message = "关系不能为空")
    private String relation;

    @Schema(description = "终点概念id", example = "")
    @NotNull(message = "终点概念id不能为空")
    private Long endConceptId;

    @Schema(description = "是否可逆", example = "")
    @NotNull(message = "是否可逆不能为空")
    private Integer inverseFlag;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;


}
