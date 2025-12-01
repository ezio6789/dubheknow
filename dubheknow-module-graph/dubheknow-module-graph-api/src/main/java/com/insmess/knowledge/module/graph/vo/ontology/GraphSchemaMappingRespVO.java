package com.insmess.knowledge.module.graph.vo.ontology;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;

/**
 * 概念映射 Response VO 对象 graph_schema_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念映射 Response VO")
@Data
public class GraphSchemaMappingRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Excel(name = "表名")
    @Schema(description = "表名", example = "")
    private String tableName;

    @Excel(name = "表显示名称")
    @Schema(description = "表显示名称", example = "")
    private String tableComment;

    @Excel(name = "实体名称列")
    @Schema(description = "实体名称列", example = "")
    private String entityNameField;

    @Excel(name = "概念id")
    @Schema(description = "概念id", example = "")
    private Long schemaId;

    @Excel(name = "概念名称")
    @Schema(description = "概念名称", example = "")
    private String schemaName;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Integer validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Integer delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
