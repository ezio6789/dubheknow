package com.insmess.knowledge.module.graph.vo.extract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 任务文件段落关联 Request VO 对象 graph_unstruct_task_text
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "任务文件段落关联 Request VO")
@Data
public class GraphUnstructTaskTextPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    private Long docId;

    @Schema(description = "段落标识", example = "")
    private Long paragraphIndex;

    @Schema(description = "文字内容", example = "")
    private String text;




}
