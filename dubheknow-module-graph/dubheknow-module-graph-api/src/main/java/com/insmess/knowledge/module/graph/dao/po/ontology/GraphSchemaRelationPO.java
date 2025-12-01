package com.insmess.knowledge.module.graph.dao.po.ontology;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 关系配置 PO 对象 graph_schema_relation
 *
 * @author insmess
 * @date 2025-11-29
 */
@Data
@TableName(value = "graph_schema_relation")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("graph_schema_relation_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphSchemaRelationPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 起点概念id */
    private Long startSchemaId;

    /** 关系 */
    private String relation;

    /** 终点概念id */
    private Long endSchemaId;

    /** 是否可逆 */
    private Integer inverseFlag;

    /** 是否有效 */
    private Integer validFlag;

    /** 删除标志 */
    @TableLogic
    private Integer delFlag;

    /** 知识库id */
    private Long knowledgeId;


}
