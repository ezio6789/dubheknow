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
 * @date 2025-12-03
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

    @Schema(description = "数据源配置(json字符串)", example = "")
    @NotBlank(message = "数据源配置(json字符串)不能为空")
    @Size(max = 1024, message = "数据源配置(json字符串)长度不能超过1024个字符")
    private String datasourceConfig;

    @Schema(description = "IP地址", example = "")
    @NotBlank(message = "IP地址不能为空")
    private String ip;

    @Schema(description = "端口号", example = "")
    @NotNull(message = "端口号不能为空")
    private Long port;

    @Schema(description = "描述", example = "")
    @NotBlank(message = "描述不能为空")
    @Size(max = 1024, message = "描述长度不能超过1024个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
