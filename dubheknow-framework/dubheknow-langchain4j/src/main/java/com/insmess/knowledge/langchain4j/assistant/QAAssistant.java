package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 知识问答专家
 */
@AiService
public interface QAAssistant {
    @SystemMessage("你是一个知识问答专家，能基于知识图谱内容进行推理回答。")
    String answer(@UserMessage String question);
}
