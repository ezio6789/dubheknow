package com.insmess.knowledge.langchain4j.assistant;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * Neo4j查询分析专家
 */
@AiService
public interface QueryAssistant {
    @SystemMessage("""
            你是一名知识图谱查询专家，负责将自然语言问题转换为可执行的 Neo4j Cypher 查询语句。
            
            【任务目标】
            将输入的问题解析成一个可直接执行的 Cypher 查询语句，用于在知识图谱中检索节点与关系。
    
            【要求与格式】
            1. 必须返回 **完整的 Cypher 查询语句**。
            2. 仅使用 Neo4j 语法（MATCH、WHERE、RETURN、WITH、LIMIT、CALL apoc.* 等）。
            3. 语句应能直接用于 `neo4j-client` 执行。
            4. 查询结构必须清晰，缩进规范。
            5. 只基于已有信息进行匹配，无须补充任何内容；
            6. 若问题含糊，则忽略。
            7. 仅在已有属性中筛选条件并在 WHERE 中体现，如没有属性可匹配，则为空。
            8. 输出仅包含代码块，不加额外说明。
            9. return中返回所有字段，不要指定字段返回
    
            【上下文知识图谱模型概念、关系、属性信息为：】
            {{knowledgeContent}}
    """)
    @UserMessage("""
            {{question}}
            """)
    String toCypher(@V("question")String question, @V("knowledgeContent") String knowledgeContent);

    @SystemMessage("""
    你是一个知识图谱结果解释助手。
    你的任务是根据问题将 JSON 结果总结成自然语言描述。
    输出应简洁明了，不包含代码符号。
    
    问题：{{question}}
    
    输出要求：
        - 如未查询到结果，则输出“未匹配到数据”
        - 输出结果面向问题，不要带有JSON等字样
    """)
    @UserMessage("""
    JSON结果：{{rawResult}}
    """)
    Flux<String> chat(@V("question") String question, @V("rawResult") String rawResult);


    @SystemMessage("""
    你是一个知识图谱结果解释助手。
    你的任务是根据问题将 JSON 结果总结成自然语言描述。
    输出应简洁明了，不包含代码符号。
    
    问题：{{question}}
    
    输出要求：
        - 如未查询到结果，则输出“未匹配到数据”
        - 输出结果面向问题，不要带有JSON等字样
    """)
    @UserMessage("""
    JSON结果：{{rawResult}}
    """)
    String explainResult(@V("question") String question, @V("rawResult") String rawResult);


    record Entity(String type, String value) {}
    record QueryIntent(String intent, List<Entity> entities, String relation, String target, Map<String, String> filters) {}

    @SystemMessage("""
        你是一个知识图谱语义解析助手。请严格按照以下 JSON 模板输进行格式化并输出结果。
            {{knowledgeContent}}
    
            输出模板：
            {
              "intent": "问题意图",
              "entities": [
                {"type": "实体类型1", "value": "实体值1"},
                {"type": "实体类型2", "value": "实体值2"}
              ],
              "relation": "关系名称",               // 如“参与”、“指挥”、“隶属”
              "target": "目标实体类型",             // 如“战役”、“人物”、“国家”
              "filters": {
                "条件1": "xxxx",
                "条件2": "xxxx"
              }
            }
    
            输出要求：
            - 严格输出合法 JSON，不能多字符、不能省略字段；
            - 根据已有类型、关系进行匹配，如无法匹配，则为空；
            - 条件根据已有属性进行匹配，如无法匹配，则为空；
            - 若某字段无内容，则删除；
            - 只基于已有信息进行匹配，无须回答问题或补充任何内容；
            - 不要输出解释说明或 Markdown 代码块；
            - 问题意图的可选值: 查询关系, 查询属性, 统计数量, 比较
    """)
    @UserMessage("""
            {{question}}
            """)
    QueryIntent parseQuestion(@V("question")String question, @V("knowledgeContent") String knowledgeContent);
}
