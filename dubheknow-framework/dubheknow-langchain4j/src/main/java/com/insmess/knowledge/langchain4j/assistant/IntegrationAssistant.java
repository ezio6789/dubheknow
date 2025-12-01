package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface IntegrationAssistant {

    @SystemMessage("你是一名知识图谱应用架构师，请输出API集成方案。")
    String designApi(@UserMessage String integrationRequest);
}
