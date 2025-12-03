package com.insmess.knowledge.module.knowbase.dao.po.unstruct;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识文件 PO 对象 knowbase_document
 *
 * @author insmess
 * @date 2025-12-03
 */
@Data
@TableName(value = "knowbase_document")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("knowbase_document_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KnowbaseDocumentPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 知识分类id */
    private Long categoryId;

    /** 知识分类名称 */
    private String categoryName;

    /** 文件名称 */
    private String name;

    /** 文件类型 */
    private String type;

    /** 解析状态 */
    private Integer parseStatus;

    /** 文件url */
    private String url;

    /** 文件描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;


}
