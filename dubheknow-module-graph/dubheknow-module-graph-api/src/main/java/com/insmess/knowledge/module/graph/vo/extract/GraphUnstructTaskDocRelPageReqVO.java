package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 任务文件关联 Request VO 对象 graph_unstruct_task_doc_rel
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "任务文件关联 Request VO")
@Data
public class GraphUnstructTaskDocRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    private Long docId;

    @Schema(description = "文件名", example = "")
    private String docName;




}
