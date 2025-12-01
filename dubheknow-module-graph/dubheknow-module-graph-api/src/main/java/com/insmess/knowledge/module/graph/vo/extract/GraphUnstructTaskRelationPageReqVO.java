package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 任务关系关联 Request VO 对象 graph_unstruct_task_relation
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "任务关系关联 Request VO")
@Data
public class GraphUnstructTaskRelationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "关系id", example = "")
    private Long relationId;




}
