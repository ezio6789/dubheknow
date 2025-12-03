package com.insmess.knowledge.module.knowbase.dao.po.unstruct;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识目录 PO 对象 knowbase_document_dir
 *
 * @author insmess
 * @date 2025-12-03
 */
@Data
@TableName(value = "knowbase_document_dir")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("knowbase_document_dir_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KnowbaseDocumentDirPO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父级id */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 显示顺序 */
    private Long orderNum;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
