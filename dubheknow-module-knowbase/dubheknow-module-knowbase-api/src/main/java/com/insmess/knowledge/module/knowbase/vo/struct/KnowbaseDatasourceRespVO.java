package com.insmess.knowledge.module.knowbase.vo.struct;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.insmess.knowledge.common.annotation.Excel;
import java.util.Date;

import java.io.Serializable;

/**
 * 数据源 Response VO 对象 knowbase_datasource
 *
 * @author insmess
 * @date 2025-12-04
 */
@Schema(description = "数据源 Response VO")
@Data
public class KnowbaseDatasourceRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "主机地址")
    @Schema(description = "主机地址", example = "")
    private String host;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    @Excel(name = "用户名")
    @Schema(description = "用户名", example = "")
    private String username;

    @Excel(name = "密码")
    @Schema(description = "密码", example = "")
    private String password;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "数据库名", readConverterExp = "模=式名")
    @Schema(description = "数据库名", example = "")
    private String schemaName;

    @Excel(name = "sid oracle数据库")
    @Schema(description = "sid oracle数据库", example = "")
    private String sid;

    @Excel(name = "是否有效;0：无效，1：有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Integer validFlag;

    @Excel(name = "删除标志;1：已删除，0：未删除")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
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
