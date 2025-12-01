package com.insmess.knowledge.module.graph.vo.extract;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;

/**
 * 非结构化抽取任务 Response VO 对象 graph_unstruct_task
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "非结构化抽取任务 Response VO")
@Data
public class GraphUnstructTaskRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "知识库id")
    @Schema(description = "知识库id", example = "")
    private Long knowledgeId;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String name;

    @Excel(name = "任务状态")
    @Schema(description = "任务状态", example = "")
    private String status;

    @Excel(name = "发布状态")
    @Schema(description = "发布状态", example = "")
    private String publishStatus;

    @Excel(name = "抽取方式。【0规则 1deepke 2大模型】")
    @Schema(description = "抽取方式。【0规则 1deepke 2大模型】", example = "")
    private Long extractMode;

    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布时间", example = "")
    private Date publishTime;

    @Excel(name = "发布人id")
    @Schema(description = "发布人id", example = "")
    private Long publisherId;

    @Excel(name = "发布人")
    @Schema(description = "发布人", example = "")
    private String publishBy;

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
