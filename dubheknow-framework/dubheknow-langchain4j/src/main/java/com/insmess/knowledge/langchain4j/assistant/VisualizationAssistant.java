package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 图谱可视化专家
 */
@AiService
public interface VisualizationAssistant {
    record GraphLayout(String layoutType, String colorStrategy, String sizeBy, String[] labelPriority) {}

    @SystemMessage("你是图谱可视化专家，请根据节点与关系信息推荐展示布局。")
    GraphLayout suggestLayout(@UserMessage String graphStats);
}
