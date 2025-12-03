package com.insmess.knowledge.module.knowbase.vo.unstruct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.insmess.knowledge.common.core.page.PageParam;

/**
 * 知识目录 Request VO 对象 knowbase_document_dir
 *
 * @author insmess
 * @date 2025-12-03
 */
@Schema(description = "知识目录 Request VO")
@Data
public class KnowbaseDocumentDirPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Schema(description = "分类名称", example = "")
    private String name;

    @Schema(description = "显示顺序", example = "")
    private Long orderNum;

}
