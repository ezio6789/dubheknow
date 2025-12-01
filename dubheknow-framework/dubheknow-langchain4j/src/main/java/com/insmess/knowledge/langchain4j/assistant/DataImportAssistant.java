package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 知识图谱数据融合专家，帮助用户清洗、映射原始数据数据导入助手
 */
@AiService
public interface DataImportAssistant {
    @SystemMessage("你是一个知识图谱数据融合专家，帮助用户清洗、映射原始数据。")
    @UserMessage("请分析以下CSV字段，建议映射到已有实体属性：")
    String suggestMapping(String csvHeader);
}
