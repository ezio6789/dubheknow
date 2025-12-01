package com.insmess.knowledge.module.graph.vo.extract;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 结构化抽取任务 Request VO 对象 graph_struct_task
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "结构化抽取任务 Request VO")
@Data
public class GraphStructTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "发布状态", example = "")
    private String publishStatus;

    @Schema(description = "发布时间", example = "")
    private Date publishTime;

    @Schema(description = "发布人id", example = "")
    private Long publisherId;

    @Schema(description = "发布人", example = "")
    private String publishBy;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "数据源名称", example = "")
    private String datasourceName;




}
