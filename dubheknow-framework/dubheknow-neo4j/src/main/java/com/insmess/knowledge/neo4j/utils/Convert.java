package com.insmess.knowledge.neo4j.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;
import com.insmess.knowledge.neo4j.domain.relationship.DynamicEntityRelationship;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.BooleanValue;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.internal.value.StringValue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


/**
 * 图谱转换器
 *
 * @author wang
 * @date 2025/03/11 17:50
 **/
public class Convert {


    /**
     * 核心转换逻辑：处理 Neo4j 的 Value 类型
     */
    public static Object convertNeo4jValue(Value value) {
        if (value == null || value.isNull()) {
            return null;
        }
        if (value instanceof StringValue) {
            return value.asString();
        } else if (value instanceof IntegerValue) {
            return value.asNumber();
        } else if (value instanceof FloatValue) {
            return value.asDouble();
        } else if (value instanceof BooleanValue) {
            return value.asBoolean();
        } else {
            return value.asObject();
        }
    }


    public static JSONObject toJSONObject(List<?> entityList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray entities = new JSONArray(); // 实体
            JSONArray relationships = new JSONArray(); // 关系
            for (Object entity : entityList) {
                // 处理实体信息
                JSONObject entityJson = new JSONObject();
                Field idField = getField(entity.getClass(), "id");
                assert idField != null;
                idField.setAccessible(true);
                Object id = idField.get(entity);
                entityJson.put("id", id);

                Field nameField = getField(entity.getClass(), "name");
                if (nameField != null) {
                    nameField.setAccessible(true);
                    Object name = nameField.get(entity);
                    entityJson.put("name", name);
                }
//                entities.add(entityJson);
                add(entities, entityJson);//添加并判断重复

                // 处理关系信息
                Field relationshipMapField = getField(entity.getClass(), "relationshipEntityMap");
                if (relationshipMapField != null) {
                    relationshipMapField.setAccessible(true);
                    Map<String, List<?>> relationshipMap = (Map<String, List<?>>) relationshipMapField.get(entity);
                    if (relationshipMap != null) {
                        for (Map.Entry<String, List<?>> entry : relationshipMap.entrySet()) {
                            String relationshipName = entry.getKey();
                            for (Object relObject : entry.getValue()) {
                                // 直接处理GraphEntityRelationship对象
                                Field endNodeField = getField(relObject.getClass(), "endNode");
                                if (endNodeField != null) {
                                    endNodeField.setAccessible(true);
                                    Object endNode = endNodeField.get(relObject);
                                    Field relationshipId = getField(relObject.getClass(), "id");
                                    assert relationshipId != null;
                                    relationshipId.setAccessible(true);
                                    JSONObject relationshipJson = new JSONObject();
                                    relationshipJson.put("id", relationshipId.get(relObject));
                                    relationshipJson.put("startId", id);
                                    if (nameField != null) {
                                        relationshipJson.put("startName", nameField.get(entity));
                                    }

                                    if (endNode != null) {
                                        Field endIdField = getField(endNode.getClass(), "id");
                                        endIdField.setAccessible(true);
                                        Long endId = (Long) endIdField.get(endNode);
                                        relationshipJson.put("endId", endId);

                                        Field endNameField = getField(endNode.getClass(), "name");
                                        if (endNameField != null) {
                                            endNameField.setAccessible(true);
                                            relationshipJson.put("endName", endNameField.get(endNode));
                                        }
                                    }
                                    relationshipJson.put("relationType", relationshipName);
//                                    relationships.add(relationshipJson);
                                    add(relationships, relationshipJson);
                                }
                            }
                        }
                    }
                }

            }
            jsonObject.put("entities", entities);
            jsonObject.put("relationships", relationships);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public static JSONObject toDynamicEntityJSONObject(List<DynamicEntity> entityList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray entities = new JSONArray(); // 实体
            JSONArray relationships = new JSONArray(); // 关系
            for (DynamicEntity entity : entityList) {
                // 处理实体信息
                JSONObject entityJson = new JSONObject();
//                Field idField = getField(entity.getClass(), "id");
//                assert idField != null;
//                idField.setAccessible(true);
//                Object id = idField.get(entity);
//                entityJson.put("id", id);
                Long id = entity.getId();
                entityJson.put("id", id);
                Map<String, Object> dynamicProperties = entity.getDynamicProperties();
                for (Map.Entry<String, Object> entry : dynamicProperties.entrySet()) {
                    String snakeKey = entry.getKey();
                    String camelKey = snakeToCamel(snakeKey);
                    if (entry.getValue() instanceof Value value) {
                        entityJson.put(camelKey, value.asObject());
                    } else {
                        entityJson.put(camelKey, entry.getValue());
                    }
                }
                if (!containsId(entities, entity.getId())) {
//                    entities.add(entityJson);
                    add(entities, entityJson);//添加并判断重复
                }

                Map<String, List<DynamicEntityRelationship>> relationshipMap = entity.getRelationshipEntityMap();
                if (relationshipMap != null) {
                    for (Map.Entry<String, List<DynamicEntityRelationship>> entry : relationshipMap.entrySet()) {
                        String relationshipName = entry.getKey();
                        for (DynamicEntityRelationship relObject : entry.getValue()) {

                            JSONObject json = new JSONObject();
                            DynamicEntity endNode = relObject.getEndNode();
                            Long endId = endNode.getId();
                            json.put("id", endId);
                            Map<String, Object> dynamicProperties2 = endNode.getDynamicProperties();
                            for (Map.Entry<String, Object> entry2 : dynamicProperties2.entrySet()) {
                                String snakeKey = entry2.getKey();
                                String camelKey = snakeToCamel(snakeKey);
                                if (entry2.getValue() instanceof Value) {
                                    json.put(camelKey, ((Value) entry2.getValue()).asObject());
                                } else {
                                    json.put(camelKey, entry2.getValue());
                                }
                            }
//                            entities.add(json);
//                            add(entities, json);
                            //封装关系
                            JSONObject relationshipJson = new JSONObject();
                            relationshipJson.put("id", relObject.getId());
                            Map<String, Object> map = endNode.getDynamicProperties();
                            String name;
                            if (map.get("name") instanceof Value) {
                                name = ((Value) map.get("name")).asString();
                            } else {
                                name = map.get("name").toString();
                            }
                            if (relObject.getDirection().equals("OUT")) {
                                relationshipJson.put("startId", id);
                                relationshipJson.put("startName", entityJson.get("name"));
                                relationshipJson.put("endId", endId);
                                relationshipJson.put("endName", name);
                            } else {
                                relationshipJson.put("startId", endId);
                                relationshipJson.put("startName", name);
                                relationshipJson.put("endId", id);
                                relationshipJson.put("endName", entityJson.get("name"));
                            }
                            relationshipJson.put("relationType", relationshipName);
//                            relationships.add(relationshipJson);
                            add(relationships, relationshipJson);
                        }
                    }
                }
            }
            jsonObject.put("entities", entities);
            jsonObject.put("relationships", relationships);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    /**
     * 添加实体，如果已经存在，则不添加
     */
    private static void add(JSONArray entities, JSONObject entity) {
        if (!containsId(entities, entity.get("id"))) {
            entities.add(entity);
        }
    }

    /**
     * 判断该id的对象在entities中是否存在
     *
     * @param entities
     * @param id
     * @return
     */
    private static boolean containsId(JSONArray entities, Object id) {
        for (Object entity : entities) {
            if (entity instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) entity;
                if (jsonObject.get("id").equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    // 将驼峰式命名转换为下划线分隔的命名
    public static String camelToSnake(String camelStr) {
        StringBuilder snakeStr = new StringBuilder();
        for (int i = 0; i < camelStr.length(); i++) {
            char c = camelStr.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                snakeStr.append("_");
            }
            snakeStr.append(Character.toLowerCase(c));
        }
        return snakeStr.toString();
    }

    // 将下划线分隔的命名转换为驼峰式命名
    public static String snakeToCamel(String snakeStr) {
        String[] components = snakeStr.split("_");
        StringBuilder camelCase = new StringBuilder(components[0]);
        for (int i = 1; i < components.length; i++) {
            String component = components[i];
            camelCase.append(Character.toUpperCase(component.charAt(0)));
            camelCase.append(component.substring(1));
        }
        return camelCase.toString();
    }
}
