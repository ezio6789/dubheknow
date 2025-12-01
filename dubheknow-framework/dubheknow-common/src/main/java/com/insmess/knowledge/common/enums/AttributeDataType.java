package com.insmess.knowledge.common.enums;


import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 概念属性数据支持类型
 */
@Getter
public enum AttributeDataType {
    STRING(0, "字符串", String.class),
    INTEGER(1, "整数", Integer.class),
    FLOAT(2, "浮点数", Double.class),
    DATETIME(3, "日期时间", LocalDateTime.class),
    BYTE(4, "字节", Byte.class),
    BOOLEAN(5, "布尔值", Boolean.class),
    // 扩展类型（需在数据库表中预留data_type=6/7/8）
    REFERENCE_OBJECT(6, "引用对象", String.class), // 存储目标节点ID
    RELATION_OBJECT(7, "关系对象", String.class),  // 存储关系ID
    REFERENCE_RELATION(8, "引用关系", String.class); // 存储关系ID

    private final int code; // 对应dataType的整数值
    private final String desc; // 类型描述
    private final Class<?> javaType; // 对应的Java类型

    AttributeDataType(int code, String desc, Class<?> javaType) {
        this.code = code;
        this.desc = desc;
        this.javaType = javaType;
    }

    /**
     * 根据dataType值获取枚举
     *
     * @param code
     * @return
     */
    public static AttributeDataType fromCode(int code) {
        for (AttributeDataType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的dataType：" + code);
    }
}
