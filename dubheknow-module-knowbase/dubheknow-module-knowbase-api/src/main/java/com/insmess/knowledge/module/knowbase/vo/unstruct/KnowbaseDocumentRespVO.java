package com.insmess.knowledge.module.knowbase.vo.unstruct;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;

/**
 * 知识文件 Response VO 对象 knowbase_document
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KnowbaseDocumentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "目录id")
    @Schema(description = "目录id", example = "")
    private Long dirId;

    @Excel(name = "目录名称")
    @Schema(description = "目录名称", example = "")
    private String dirName;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "")
    private String name;

    @Excel(name = "文件类型")
    @Schema(description = "文件类型", example = "")
    private String type;

    @Excel(name = "解析状态")
    @Schema(description = "解析状态", example = "")
    private Integer parseStatus;

    @Excel(name = "文件url")
    @Schema(description = "文件url", example = "")
    private String url;

    @Excel(name = "文件描述")
    @Schema(description = "文件描述", example = "")
    private String description;

    @Excel(name = "是否有效;0：无效，1：有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志;1：已删除，0：未删除")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

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
