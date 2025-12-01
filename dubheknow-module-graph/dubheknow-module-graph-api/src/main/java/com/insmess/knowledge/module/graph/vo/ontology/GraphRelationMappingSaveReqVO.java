package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 关系映射 创建/修改 Request VO graph_relation_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "关系映射 Response VO")
@Data
public class GraphRelationMappingSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "表名", example = "")
    @NotBlank(message = "表名不能为空")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    @NotBlank(message = "表显示名称不能为空")
    @Size(max = 128, message = "表显示名称长度不能超过128个字符")
    private String tableComment;

    @Schema(description = "字段名", example = "")
    @NotBlank(message = "字段名不能为空")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    @NotBlank(message = "字段显示名称不能为空")
    @Size(max = 256, message = "字段显示名称长度不能超过256个字符")
    private String fieldComment;

    @Schema(description = "关系", example = "")
    @NotBlank(message = "关系不能为空")
    private String relation;

    @Schema(description = "关联表", example = "")
    @NotBlank(message = "关联表不能为空")
    @Size(max = 256, message = "关联表长度不能超过256个字符")
    private String relationTable;

    @Schema(description = "关联表名称", example = "")
    @NotBlank(message = "关联表名称不能为空")
    @Size(max = 128, message = "关联表名称长度不能超过128个字符")
    private String relationTableName;

    @Schema(description = "关联表字段", example = "")
    @NotBlank(message = "关联表字段不能为空")
    @Size(max = 128, message = "关联表字段长度不能超过128个字符")
    private String relationField;

    @Schema(description = "关联表实体字段", example = "")
    @NotBlank(message = "关联表实体字段不能为空")
    @Size(max = 128, message = "关联表实体字段长度不能超过128个字符")
    private String relationNameField;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
