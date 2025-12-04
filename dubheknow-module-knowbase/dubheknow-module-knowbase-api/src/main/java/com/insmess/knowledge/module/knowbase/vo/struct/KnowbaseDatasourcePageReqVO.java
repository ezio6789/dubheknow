package com.insmess.knowledge.module.knowbase.vo.struct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 数据源 Request VO 对象 knowbase_datasource
 *
 * @author insmess
 * @date 2025-12-04
 */
@Schema(description = "数据源 Request VO")
@Data
public class KnowbaseDatasourcePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "主机地址", example = "")
    private String host;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "用户名", example = "")
    private String username;

    @Schema(description = "密码", example = "")
    private String password;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "数据库名", example = "")
    private String schemaName;

    @Schema(description = "sid oracle数据库", example = "")
    private String sid;




}
