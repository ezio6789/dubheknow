package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SimpleAssistant {
    @SystemMessage("你是一个通用智能助手。")
    String chat(@UserMessage String question);
}
