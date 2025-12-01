package com.insmess.knowledge.module.graph.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 知识图谱 Request VO 对象 graph_knowledge
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "知识图谱 Request VO")
@Data
public class GraphKnowledgePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "知识图谱名称", example = "")
    private String name;

    @Schema(description = "知识图谱描述", example = "")
    private String description;




}
