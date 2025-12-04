package com.insmess.knowledge.neo4j.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;
import com.insmess.knowledge.neo4j.domain.relationship.DynamicEntityRelationship;
import org.neo4j.driver.Value;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Convert {

    private Convert() {
    }

    public static Object convertNeo4jValue(Value value) {
        if (value == null || value.isNull()) {
            return null;
        }
        return switch (value.type().name()) {
            case "STRING" -> value.asString();
            case "INTEGER" -> value.asNumber();
            case "FLOAT", "DOUBLE" -> value.asDouble();
            case "BOOLEAN" -> value.asBoolean();
            default -> value.asObject();
        };
    }

    public static JSONObject toJSONObject(List<?> entityList) {
        JSONObject graph = emptyGraphJson();
        JSONArray entities = graph.getJSONArray("entities");
        JSONArray relationships = graph.getJSONArray("relationships");

        for (Object entity : entityList) {
            try {
                Object id = readField(entity, "id");
                Object name = readField(entity, "name");

                JSONObject entityJson = new JSONObject();
                entityJson.put("id", id);
                if (name != null) {
                    entityJson.put("name", name);
                }
                addIfMissing(entities, entityJson);

                Map<String, List<?>> relationMap = castRelationshipMap(readField(entity, "relationshipEntityMap"));
                if (relationMap == null) {
                    continue;
                }
                for (Map.Entry<String, List<?>> entry : relationMap.entrySet()) {
                    String relationName = entry.getKey();
                    for (Object relObject : entry.getValue()) {
                        Object relId = readField(relObject, "id");
                        Object endNode = readField(relObject, "endNode");

                        JSONObject relJson = new JSONObject();
                        relJson.put("id", relId);
                        relJson.put("relationType", relationName);
                        relJson.put("startId", id);
                        relJson.put("startName", name);
                        if (endNode != null) {
                            Object endId = readField(endNode, "id");
                            Object endName = readField(endNode, "name");
                            relJson.put("endId", endId);
                            if (endName != null) {
                                relJson.put("endName", endName);
                            }
                        }
                        addIfMissing(relationships, relJson);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to convert entity to JSON", e);
            }
        }
        return graph;
    }

    public static JSONObject toDynamicEntityJSONObject(List<DynamicEntity> entityList) {
        JSONObject graph = emptyGraphJson();
        JSONArray entities = graph.getJSONArray("entities");
        JSONArray relationships = graph.getJSONArray("relationships");

        for (DynamicEntity entity : entityList) {
            JSONObject entityJson = new JSONObject();
            entityJson.put("id", entity.getId());
            copyDynamicProperties(entityJson, entity.getDynamicProperties());
            addIfMissing(entities, entityJson);

            Map<String, List<DynamicEntityRelationship>> relationMap = entity.getRelationshipEntityMap();
            if (relationMap == null) {
                continue;
            }
            for (Map.Entry<String, List<DynamicEntityRelationship>> entry : relationMap.entrySet()) {
                String relationName = entry.getKey();
                for (DynamicEntityRelationship relObject : entry.getValue()) {
                    DynamicEntity endNode = relObject.getEndNode();

                    JSONObject endNodeJson = new JSONObject();
                    endNodeJson.put("id", endNode.getId());
                    copyDynamicProperties(endNodeJson, endNode.getDynamicProperties());
                    addIfMissing(entities, endNodeJson);

                    JSONObject relJson = new JSONObject();
                    relJson.put("id", relObject.getId());
                    relJson.put("relationType", relationName);

                    boolean outgoing = "OUT".equals(relObject.getDirection());
                    relJson.put("startId", outgoing ? entity.getId() : endNode.getId());
                    relJson.put("endId", outgoing ? endNode.getId() : entity.getId());
                    relJson.put("startName", outgoing ? entityJson.get("name") : endNodeJson.get("name"));
                    relJson.put("endName", outgoing ? endNodeJson.get("name") : entityJson.get("name"));

                    addIfMissing(relationships, relJson);
                }
            }
        }
        return graph;
    }

    private static JSONObject emptyGraphJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entities", new JSONArray());
        jsonObject.put("relationships", new JSONArray());
        return jsonObject;
    }

    private static void addIfMissing(JSONArray entities, JSONObject entity) {
        if (!containsId(entities, entity.get("id"))) {
            entities.add(entity);
        }
    }

    private static void copyDynamicProperties(JSONObject target, Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String camelKey = snakeToCamel(entry.getKey());
            target.put(camelKey, unwrapDynamicValue(entry.getValue()));
        }
    }

    private static Object unwrapDynamicValue(Object value) {
        if (value instanceof Value neo4jValue) {
            return convertNeo4jValue(neo4jValue);
        }
        return value;
    }

    private static boolean containsId(JSONArray entities, Object id) {
        for (Object entity : entities) {
            if (entity instanceof JSONObject jsonObject) {
                Object existingId = jsonObject.get("id");
                if (Objects.equals(existingId, id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static Object readField(Object target, String fieldName) throws IllegalAccessException {
        Field field = findField(target.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        return field.get(target);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, List<?>> castRelationshipMap(Object relationshipMap) {
        return (Map<String, List<?>>) relationshipMap;
    }

    // camelCase -> snake_case
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

    // snake_case -> camelCase
    public static String snakeToCamel(String snakeStr) {
        String[] components = snakeStr.split("_");
        if (components.length == 0) {
            return snakeStr;
        }
        StringBuilder camelCase = new StringBuilder(components[0]);
        for (int i = 1; i < components.length; i++) {
            String component = components[i];
            if (component.isEmpty()) {
                continue;
            }
            camelCase.append(Character.toUpperCase(component.charAt(0)));
            camelCase.append(component.substring(1));
        }
        return camelCase.toString();
    }
}
