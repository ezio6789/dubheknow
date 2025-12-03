package com.insmess.knowledge.module.knowbase.dao.po.unstruct;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 知识内容 PO 对象 knowbase_document_content
 *
 * @author insmess
 * @date 2025-12-03
 */
@Data
@TableName(value = "knowbase_document_content")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("knowbase_document_content_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowbaseDocumentContentPO {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 知识文档id */
    private Long documentId;

    /** 知识内容（解析结果） */
    private String content;


}
