package com.insmess.knowledge.langchain4j.assistant;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * Neo4j 知识抽取助手
 */
@AiService
public interface ExtractAssistant {
    // 实体
    record ExtractedEntity(String id, String concept, String schema, Map<String, String> attributes) {}

    // 关系
    record ExtractedRelation(String sourceEntityId, String targetEntityId, String relationType) {}

    // 包含实体和关系的结果
    record ExtractionResult(
            List<ExtractedEntity> entities,
            List<ExtractedRelation> relations
    ) {}

    @SystemMessage("""
        你是一个信息抽取助手。请从以下文本中抽取出所有符合定义的【概念、属性和关系】，并返回符合要求的合法 JSON 格式。
    
        已有概念关系（概念-关系-概念）：
        {{schemaRelations}}
  
        已有概念属性（概念[属性1,属性2,属性3]）：
        {{schemaAttributes}}
    
        输出 JSON 格式：
        {
          "entities": [
            {
              "id": "实体标识",
              "concept": "实体名称",
              "schema": "实体所属概念名称",
              "attributes": {
                "属性名1": "属性值1",
                "属性名2": "属性值2",
                ...
              }
            }
          ],
          "relations": [
            {
              "sourceEntityId": "源实体标识",
              "targetEntityId": "目标实体标识",
              "relationType": "关系类型"
            }
          ]
    """)
    @UserMessage("""
            要提取的文本为：{{question}}
            """)
    ExtractionResult extract(@V("question")String question,
                                       @V("schemaRelations") String schemaRelations,
                                       @V("schemaAttributes") String schemaAttributes);
}
