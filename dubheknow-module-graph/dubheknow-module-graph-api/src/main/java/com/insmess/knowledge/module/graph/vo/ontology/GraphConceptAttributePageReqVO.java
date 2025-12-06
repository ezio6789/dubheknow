package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 概念属性 Request VO 对象 graph_concept_attribute
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念属性 Request VO")
@Data
public class GraphConceptAttributePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "概念id", example = "")
    private Long conceptId;

    @Schema(description = "概念名称", example = "")
    private String conceptName;

    @Schema(description = "属性名称", example = "")
    private String name;

    @Schema(description = "属性名称代码", example = "")
    private String nameCode;

    @Schema(description = "是否必填", example = "")
    private Integer requireFlag;

    @Schema(description = "数据类型", example = "")
    private String dataType;

    @Schema(description = "单/多值", example = "")
    private String multipleFlag;

    @Schema(description = "校验方式", example = "")
    private String validateType;

    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Schema(description = "最大值", example = "")
    private Long maxValue;




}
