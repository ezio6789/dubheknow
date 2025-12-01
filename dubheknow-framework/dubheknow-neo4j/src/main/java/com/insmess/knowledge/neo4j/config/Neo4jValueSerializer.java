package com.insmess.knowledge.neo4j.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.neo4j.driver.Value;

import java.io.IOException;

/**
 * Neo4j Value对象的Jackson序列化器
 */
public class Neo4jValueSerializer extends JsonSerializer<Value> {

    @Override
    public void serialize(Value value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // 根据Value类型进行序列化，避免调用会抛异常的方法
        if (value.isNull()) {
            gen.writeNull();
        } else {
            // 使用asObject()方法获取Java原生类型进行序列化
            Object obj = value.asObject();
            gen.writeObject(obj);
        }
    }
}
