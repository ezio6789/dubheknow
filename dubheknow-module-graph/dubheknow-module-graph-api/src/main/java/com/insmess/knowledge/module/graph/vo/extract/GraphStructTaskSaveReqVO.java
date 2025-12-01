package com.insmess.knowledge.module.graph.vo.extract;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 结构化抽取任务 创建/修改 Request VO graph_struct_task
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "结构化抽取任务 Response VO")
@Data
public class GraphStructTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Schema(description = "任务名称", example = "")
    @NotBlank(message = "任务名称不能为空")
    private String name;

    @Schema(description = "任务状态", example = "")
    @NotBlank(message = "任务状态不能为空")
    private String status;

    @Schema(description = "发布状态", example = "")
    @NotBlank(message = "发布状态不能为空")
    private String publishStatus;

    @Schema(description = "发布时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Schema(description = "发布人id", example = "")
    private Long publisherId;

    @Schema(description = "发布人", example = "")
    @NotBlank(message = "发布人不能为空")
    @Size(max = 128, message = "发布人长度不能超过128个字符")
    private String publishBy;

    @Schema(description = "数据源id", example = "")
    @NotNull(message = "数据源id不能为空")
    private Long datasourceId;

    @Schema(description = "数据源名称", example = "")
    @NotBlank(message = "数据源名称不能为空")
    private String datasourceName;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
