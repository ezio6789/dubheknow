package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 属性映射 Request VO 对象 graph_attribute_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "属性映射 Request VO")
@Data
public class GraphAttributeMappingPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "表名", example = "")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    private String tableComment;

    @Schema(description = "字段名", example = "")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    private String fieldComment;

    @Schema(description = "属性id", example = "")
    private Long attributeId;

    @Schema(description = "属性名称", example = "")
    private String attributeName;




}
