package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 属性映射 创建/修改 Request VO graph_struct_task_attribute_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "属性映射 Response VO")
@Data
public class GraphStructTaskAttributeMappingSaveReqVO extends BaseEntity {

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

    @Schema(description = "属性id", example = "")
    private Long attributeId;

    @Schema(description = "属性名称", example = "")
    @NotBlank(message = "属性名称不能为空")
    @Size(max = 128, message = "属性名称长度不能超过128个字符")
    private String attributeName;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
