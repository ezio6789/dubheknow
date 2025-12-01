package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 任务文件段落关联 创建/修改 Request VO graph_unstruct_task_text
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "任务文件段落关联 Response VO")
@Data
public class GraphUnstructTaskTextSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    @NotNull(message = "文件id不能为空")
    private Long docId;

    @Schema(description = "段落标识", example = "")
    private Long paragraphIndex;

    @Schema(description = "文字内容", example = "")
    @NotBlank(message = "文字内容不能为空")
    private String text;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
