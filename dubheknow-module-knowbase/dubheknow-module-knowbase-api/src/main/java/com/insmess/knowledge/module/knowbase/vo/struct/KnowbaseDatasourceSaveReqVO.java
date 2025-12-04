package com.insmess.knowledge.module.knowbase.vo.struct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 数据源 创建/修改 Request VO knowbase_datasource
 *
 * @author insmess
 * @date 2025-12-04
 */
@Schema(description = "数据源 Response VO")
@Data
public class KnowbaseDatasourceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据源名称", example = "")
    @NotBlank(message = "数据源名称不能为空")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    @NotBlank(message = "数据源类型不能为空")
    private String datasourceType;

    @Schema(description = "主机地址", example = "")
    @NotBlank(message = "主机地址不能为空")
    private String host;

    @Schema(description = "端口号", example = "")
    @NotNull(message = "端口号不能为空")
    private Long port;

    @Schema(description = "用户名", example = "")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 255, message = "用户名长度不能超过255个字符")
    private String username;

    @Schema(description = "密码", example = "")
    @NotBlank(message = "密码不能为空")
    @Size(max = 255, message = "密码长度不能超过255个字符")
    private String password;

    @Schema(description = "描述", example = "")
    @NotBlank(message = "描述不能为空")
    @Size(max = 1024, message = "描述长度不能超过1024个字符")
    private String description;

    @Schema(description = "数据库名", example = "")
    @NotBlank(message = "数据库名不能为空")
    @Size(max = 255, message = "数据库名长度不能超过255个字符")
    private String schemaName;

    @Schema(description = "sid oracle数据库", example = "")
    @Size(max = 255, message = "sid oracle数据库长度不能超过255个字符")
    private String sid;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
