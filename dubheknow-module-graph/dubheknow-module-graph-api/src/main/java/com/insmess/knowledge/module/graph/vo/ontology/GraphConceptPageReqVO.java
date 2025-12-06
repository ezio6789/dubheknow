package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 概念配置 Request VO 对象 graph_concept
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念配置 Request VO")
@Data
public class GraphConceptPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "概念名称", example = "")
    private String name;

    @Schema(description = "概念描述", example = "")
    private String description;

    @Schema(description = "概念颜色", example = "")
    private String color;



    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Schema(description = "父概念id", example = "")
    private Long pid;


}
