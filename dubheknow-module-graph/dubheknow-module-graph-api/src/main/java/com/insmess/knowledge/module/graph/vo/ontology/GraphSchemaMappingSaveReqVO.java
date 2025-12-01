package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 概念映射 创建/修改 Request VO graph_schema_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念映射 Response VO")
@Data
public class GraphSchemaMappingSaveReqVO extends BaseEntity {

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

    @Schema(description = "实体名称列", example = "")
    @NotBlank(message = "实体名称列不能为空")
    @Size(max = 32, message = "实体名称列长度不能超过32个字符")
    private String entityNameField;

    @Schema(description = "概念id", example = "")
    private Long schemaId;

    @Schema(description = "概念名称", example = "")
    @NotBlank(message = "概念名称不能为空")
    @Size(max = 128, message = "概念名称长度不能超过128个字符")
    private String schemaName;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
