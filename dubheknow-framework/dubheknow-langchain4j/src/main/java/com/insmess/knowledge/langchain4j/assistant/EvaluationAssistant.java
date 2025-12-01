package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 一位知识图谱质量评估专家
 */
@AiService
public interface EvaluationAssistant {

    record EvaluationReport(double completeness, double consistency, String[] issues) {}

    @SystemMessage("你是一位知识图谱质量评估专家，请对图谱样本数据进行评估。")
    EvaluationReport evaluate(@UserMessage String graphSample);
}
