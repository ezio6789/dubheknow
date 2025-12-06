package com.insmess.knowledge.module.graph.dao.po.ontology;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 概念配置 PO 对象 graph_concept
 *
 * @author insmess
 * @date 2025-11-29
 */
@Data
@TableName(value = "graph_concept")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("graph_concept_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphConceptPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 概念名称 */
    private String name;

    /** 概念描述 */
    private String description;

    /** 概念颜色 */
    private String color;

    /** 是否有效 */
    private Integer validFlag;

    /** 删除标志 */
    @TableLogic
    private Integer delFlag;

    /** 知识库id */
    private Long knowledgeId;

    /** 父概念id */
    private Long pid;

    /*子对象集合*/
    @TableField(exist = false)
    private List<GraphConceptPO> children;

    /*属性列表*/
    @TableField(exist = false)
    private List<GraphConceptAttributePO> properties;

}
