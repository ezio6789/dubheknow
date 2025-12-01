package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;


/**
 * 歧义消岐
 */
@AiService
public interface DisambiguationAssistant {
    record DisambiguationResult(String canonicalName, double confidence, String reason) {}

    @SystemMessage("你是一个知识融合专家，负责消除实体歧义。")
    @UserMessage("以下是多个候选实体描述，请判断最可能的同一实体：")
    DisambiguationResult disambiguate(String candidateDescriptions);
}
