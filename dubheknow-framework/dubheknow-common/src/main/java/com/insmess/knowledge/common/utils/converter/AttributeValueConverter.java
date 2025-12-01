package com.insmess.knowledge.common.utils.converter;

import com.insmess.knowledge.common.enums.AttributeDataType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AttributeValueConverter {

    /**
     * 将Java对象转换为Neo4j可存储的格式
     *
     * @param value    原始值（如LocalDateTime、Integer）
     * @param dataType 属性类型（来自ext_schema_attribute）
     * @return Neo4j存储值（如时间戳、Long）
     */
    public static Object toNeo4jValue(Object value, AttributeDataType dataType) {
        if (value == null) {
            return null;
        }

        switch (dataType) {
            case STRING:
                return value.toString(); // 文本直接存字符串
            case INTEGER:
                return ((Number) value).longValue(); // 整数存为Long（避免溢出）
            case FLOAT:
                return ((Number) value).doubleValue(); // 小数存为Double
            case DATETIME:
                // 日期时间转为UTC时间戳（毫秒）
                LocalDateTime dateTime = (LocalDateTime) value;
                return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            case BYTE:
                return value; // 字节数组直接存储（Neo4j支持byte[]）
            case BOOLEAN:
                return value; // 布尔值直接存储
            case REFERENCE_OBJECT:
            case RELATION_OBJECT:
            case REFERENCE_RELATION:
                // 引用类型存储目标ID（Long）
                return ((Number) value).longValue();
            default:
                throw new IllegalArgumentException("不支持的转换类型：" + dataType);
        }
    }

    /**
     * 将Neo4j存储值转换为Java对象（根据元数据类型）
     *
     * @param neo4jValue Neo4j中的存储值
     * @param dataType   属性类型
     * @return 原始Java类型对象
     */
    public static Object fromNeo4jValue(Object neo4jValue, AttributeDataType dataType) {
        if (neo4jValue == null) {
            return null;
        }

        switch (dataType) {
            case STRING:
                return neo4jValue.toString();
            case INTEGER:
                return ((Number) neo4jValue).intValue();
            case FLOAT:
                return ((Number) neo4jValue).doubleValue();
            case DATETIME:
                // 时间戳转为LocalDateTime（UTC）
                Long timestamp = ((Number) neo4jValue).longValue();
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
            case BYTE:
                return neo4jValue; // 直接返回字节数组
            case BOOLEAN:
                return neo4jValue; // 直接返回布尔值
            case REFERENCE_OBJECT:
            case RELATION_OBJECT:
            case REFERENCE_RELATION:
                return ((Number) neo4jValue).longValue(); // 返回引用ID
            default:
                throw new IllegalArgumentException("不支持的转换类型：" + dataType);
        }
    }
}