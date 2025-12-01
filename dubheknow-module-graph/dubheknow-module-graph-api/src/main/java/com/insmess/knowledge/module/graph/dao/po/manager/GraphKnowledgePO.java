package com.insmess.knowledge.module.graph.dao.po.manager;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识图谱 PO 对象 graph_knowledge
 *
 * @author insmess
 * @date 2025-11-29
 */
@Data
@TableName(value = "graph_knowledge")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("graph_knowledge_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphKnowledgePO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 知识图谱名称 */
    private String name;

    /** 知识图谱描述 */
    private String description;

    /** 是否有效； 0：无效 1：有效 */
    private Long validFlag;

    /** 删除标志；1 已删除 0未删除 */
    @TableLogic
    private Long delFlag;


}
