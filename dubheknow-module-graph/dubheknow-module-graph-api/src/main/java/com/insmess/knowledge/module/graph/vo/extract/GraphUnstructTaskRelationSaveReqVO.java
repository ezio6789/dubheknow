package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 任务关系关联 创建/修改 Request VO graph_unstruct_task_relation
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "任务关系关联 Response VO")
@Data
public class GraphUnstructTaskRelationSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "关系id", example = "")
    @NotNull(message = "关系id不能为空")
    private Long relationId;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
