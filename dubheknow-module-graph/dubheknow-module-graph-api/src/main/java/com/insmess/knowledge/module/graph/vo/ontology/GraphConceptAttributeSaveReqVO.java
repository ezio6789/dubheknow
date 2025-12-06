package com.insmess.knowledge.module.graph.vo.ontology;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 概念属性 创建/修改 Request VO graph_concept_attribute
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念属性 Response VO")
@Data
public class GraphConceptAttributeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "概念id", example = "")
    @NotNull(message = "概念id不能为空")
    private Long conceptId;

    @Schema(description = "概念名称", example = "")
    @NotBlank(message = "概念名称不能为空")
    private String conceptName;

    @Schema(description = "属性名称", example = "")
    @NotBlank(message = "属性名称不能为空")
    private String name;

    @Schema(description = "属性名称代码", example = "")
    @NotBlank(message = "属性名称代码不能为空")
    private String nameCode;

    @Schema(description = "是否必填", example = "")
    @NotNull(message = "是否必填不能为空")
    private Integer requireFlag;

    @Schema(description = "数据类型", example = "")
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    @Schema(description = "单/多值", example = "")
    @NotBlank(message = "单/多值不能为空")
    private String multipleFlag;

    @Schema(description = "校验方式", example = "")
    @NotBlank(message = "校验方式不能为空")
    private String validateType;

    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Schema(description = "最大值", example = "")
    private Long maxValue;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
