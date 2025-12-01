package com.insmess.knowledge.langchain4j.assistant;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 知识图谱本体设计
 */
@AiService
public interface SchemaAssistant {

    record EntitySchema(String entityName, String description, String[] attributes) {}

    @SystemMessage("你是一名知识图谱本体设计专家，擅长定义实体类型、属性、关系。")
    @UserMessage("请根据以下领域描述，生成实体定义与主要属性。")
    EntitySchema designSchema(String domainDescription);
}
