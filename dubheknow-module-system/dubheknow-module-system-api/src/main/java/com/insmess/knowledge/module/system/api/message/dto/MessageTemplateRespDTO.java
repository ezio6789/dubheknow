package com.insmess.knowledge.module.system.api.message.dto;

import lombok.*;

/**
 * 消息模板 DTO 对象 message_template
 *
 * @author insmess
 * @date 2024-10-31
 */
@Data
public class MessageTemplateRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 消息标题 */
    private String title;

    /** 消息模板内容 */
    private String content;

    /** 消息类别 */
    private Integer category;

    /** 消息等级 */
    private Integer msgLevel;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
