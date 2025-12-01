package com.insmess.knowledge.langchain4j.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * 数字地球相关的助手
 */
@AiService
public interface EarthAssistant {

    @SystemMessage("""
    你是一名知识图谱与数字地球融合的智能转换助手，专门负责将知识图谱中的实体信息转换为数字地球可展示的地理数据。
    
    你的任务：
    1. 从输入的实体信息中识别出**具有地理意义的实体**，包括但不限于：
       - 国家、城市、地区、地点
       - 战役、事件、作战行动
       - 机场、基地、海域、山脉、河流
    2. 对于这些地理实体，提取或推理出其经纬度坐标。
    3. 对于**无地理意义的实体**（如人物、组织、条约、战略、武器型号等），**不要输出**。
    4. 输出格式必须为严格的 JSON 数组，每个对象包含：
       - entity_id：实体的唯一标识
       - entity_name：实体名称
       - coordinates：经纬度数组 [lon, lat]
    5. 经纬度请尽可能基于实体名称或上下文推理（如“法国诺曼底”→[-0.5, 49.3]）。
    6. 不要输出任何解释、注释或额外文字。
    7. 如果没有符合条件的实体，请输出空数组 []。
    """)
    @UserMessage("""
    以下是从知识图谱查询出的实体信息，请仅输出适合数字地球展示的实体坐标数据：
    
    {{entitiesJson}}
    
    输出格式示例：
    [
      {"entity_id": "e1", "entity_name": "诺曼底登陆战役", "coordinates": [-0.5, 49.3]},
      {"entity_id": "e2", "entity_name": "柏林", "coordinates": [13.4050, 52.5200]}
    ]
    """)
    String parseQuestion(@V("entitiesJson")String entitiesJson);
}
