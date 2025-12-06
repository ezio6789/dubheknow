package com.insmess.knowledge.module.graph.vo.ontology;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptPO;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;
import java.util.List;

/**
 * 概念配置 Response VO 对象 graph_concept
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "概念配置 Response VO")
@Data
public class GraphConceptRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "概念名称")
    @Schema(description = "概念名称", example = "")
    private String name;

    @Excel(name = "概念描述")
    @Schema(description = "概念描述", example = "")
    private String description;

    @Excel(name = "概念颜色")
    @Schema(description = "概念颜色", example = "")
    private String color;

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

    @Excel(name = "知识库id")
    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Excel(name = "父概念id")
    @Schema(description = "父概念id", example = "")
    private Long pid;

    @Schema(description = "父概念id", example = "")
    private List<GraphConceptPO> children;

}
