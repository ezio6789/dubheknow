package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 概念映射 Request VO 对象 graph_concept_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念映射 Request VO")
@Data
public class GraphConceptMappingPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "表名", example = "")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    private String tableComment;

    @Schema(description = "实体名称列", example = "")
    private String entityNameField;

    @Schema(description = "概念id", example = "")
    private Long conceptId;

    @Schema(description = "概念名称", example = "")
    private String conceptName;




}
