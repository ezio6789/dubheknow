package com.insmess.knowledge.module.graph.vo.ontology;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 关系配置 Request VO 对象 graph_concept_relation
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "关系配置 Request VO")
@Data
public class GraphConceptRelationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "起点概念id", example = "")
    private Long startConceptId;

    @Schema(description = "关系", example = "")
    private String relation;

    @Schema(description = "终点概念id", example = "")
    private Long endConceptId;

    @Schema(description = "是否可逆", example = "")
    private Integer inverseFlag;

    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @TableField(exist = false)
    private Long startEndId;
}
