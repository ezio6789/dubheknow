package com.insmess.knowledge.module.graph.dao.po.ontology;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 概念映射 PO 对象 graph_schema_mapping
 *
 * @author insmess
 * @date 2025-11-29
 */
@Data
@TableName(value = "graph_schema_mapping")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("graph_schema_mapping_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphSchemaMappingPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务id */
    private Long taskId;

    /** 表名 */
    private String tableName;

    /** 表显示名称 */
    private String tableComment;

    /** 实体名称列 */
    private String entityNameField;

    /** 概念id */
    private Long schemaId;

    /** 概念名称 */
    private String schemaName;

    /** 是否有效 */
    private Integer validFlag;

    /** 删除标志 */
    @TableLogic
    private Integer delFlag;


}
