package com.insmess.knowledge.module.graph.dao.po.extract;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 非结构化抽取任务 PO 对象 graph_unstruct_task
 *
 * @author insmess
 * @date 2025-11-29
 */
@Data
@TableName(value = "graph_unstruct_task")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("graph_unstruct_task_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphUnstructTaskPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 知识库id */
    private Long knowledgeId;

    /** 任务名称 */
    private String name;

    /** 任务状态 */
    private String status;

    /** 发布状态 */
    private String publishStatus;

    /** 抽取方式。【0规则 1deepke 2大模型】 */
    private Long extractMode;

    /** 发布时间 */
    private Date publishTime;

    /** 发布人id */
    private Long publisherId;

    /** 发布人 */
    private String publishBy;

    /** 是否有效 */
    private Integer validFlag;

    /** 删除标志 */
    @TableLogic
    private Integer delFlag;


}
