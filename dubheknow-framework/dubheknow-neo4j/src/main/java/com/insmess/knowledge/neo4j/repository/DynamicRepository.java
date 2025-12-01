package com.insmess.knowledge.neo4j.repository;

import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.common.utils.spring.SpringUtils;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;
import com.insmess.knowledge.neo4j.domain.relationship.DynamicEntityRelationship;
import com.insmess.knowledge.neo4j.wrapper.Neo4jQueryWrapper;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface DynamicRepository extends BaseRepository<DynamicEntity, Long> {

    default List<DynamicEntity> findStep(Neo4jQueryWrapper<DynamicEntity> wrapper, Long id) {
        String baseMatch = wrapper.getCypherStepQuery(id);
        Map<String, Object> params = new HashMap<>(wrapper.getParams());
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        List<DynamicEntity> rows = new ArrayList<>();

        neo4jClient.query(baseMatch)
                .bindAll(params)
                .fetch()
                .all()
                .forEach(record -> {
                    List<Object> nodeList = (List<Object>) record.get("nodes");
                    List<Object> relList = (List<Object>) record.get("relationships");

                    if (nodeList == null || nodeList.isEmpty()) return;

                    // === 封装节点 ===
                    Map<Long, DynamicEntity> entityMap = new HashMap<>();
                    for (Object nObj : nodeList) {
                        if (nObj instanceof org.neo4j.driver.types.Node n) {
                            DynamicEntity entity = new DynamicEntity();
                            entity.setId(n.id());
                            entity.setLabels(StreamSupport.stream(n.labels().spliterator(), false)
                                    .collect(Collectors.toSet()));
                            entity.setRelationshipEntityMap(new HashMap<>());

                            Map<String, Object> props = n.asMap();
                            Map<String, Object> dynamicProps = new HashMap<>();

                            // 提取 dynamicProperties_ 开头的属性
                            for (Map.Entry<String, Object> entry : props.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (key.startsWith("dynamicProperties_")) {
                                    String strippedKey = key.substring("dynamicProperties_".length());
                                    dynamicProps.put(strippedKey, value);
                                } else {
                                    if ("name".equals(key)) {
                                        entity.setName(value != null ? value.toString() : "");
                                    }
                                }
                            }

                            entity.setDynamicProperties(dynamicProps);
                            entityMap.put(n.id(), entity);
                        }
                    }

                    // === 封装关系 ===
                    for (Object rObj : relList) {
                        if (rObj instanceof org.neo4j.driver.types.Relationship rel) {
                            long startId = rel.startNodeId();
                            long endId = rel.endNodeId();
                            String type = rel.type();
                            long relId = rel.id();

                            DynamicEntity startNode = entityMap.get(startId);
                            DynamicEntity endNode = entityMap.get(endId);
                            if (startNode == null || endNode == null) continue;

                            DynamicEntityRelationship relationship = new DynamicEntityRelationship();
                            relationship.setId(relId);
                            relationship.setEndNode(endNode);
                            relationship.setDirection("OUT");
                            startNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(relationship);

                            DynamicEntityRelationship reverse = new DynamicEntityRelationship();
                            reverse.setId(relId);
                            reverse.setEndNode(startNode);
                            reverse.setDirection("IN");
                            endNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(reverse);
                        }
                    }

                    rows.addAll(entityMap.values());
                });

        return rows;
    }

    default PageResult<DynamicEntity> findByEntityId(Neo4jQueryWrapper<DynamicEntity> wrapper) {
        String baseMatch = wrapper.getCypherByEntityIdQuery();
        Map<String, Object> params = new HashMap<>(wrapper.getParams());
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        List<DynamicEntity> rows = new ArrayList<>();

        neo4jClient.query(baseMatch)
                .bindAll(params)
                .fetch()
                .all()
                .forEach(record -> {
                    List<Object> nodeList = (List<Object>) record.get("nodes");
                    List<Object> relList = (List<Object>) record.get("relationships");

                    if (nodeList == null || nodeList.isEmpty()) return;

                    // === 封装节点 ===
                    Map<Long, DynamicEntity> entityMap = new HashMap<>();
                    for (Object nObj : nodeList) {
                        if (nObj instanceof org.neo4j.driver.types.Node n) {
                            DynamicEntity entity = new DynamicEntity();
                            entity.setId(n.id());
                            entity.setLabels(StreamSupport.stream(n.labels().spliterator(), false)
                                    .collect(Collectors.toSet()));
                            entity.setRelationshipEntityMap(new HashMap<>());

                            Map<String, Object> props = n.asMap();
                            Map<String, Object> dynamicProps = new HashMap<>();

                            // 提取 dynamicProperties_ 开头的属性
                            for (Map.Entry<String, Object> entry : props.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (key.startsWith("dynamicProperties_")) {
                                    String strippedKey = key.substring("dynamicProperties_".length());
                                    dynamicProps.put(strippedKey, value);
                                } else {
                                    if ("name".equals(key)) {
                                        entity.setName(value != null ? value.toString() : "");
                                    }
                                }
                            }

                            entity.setDynamicProperties(dynamicProps);
                            entityMap.put(n.id(), entity);
                        }
                    }

                    // === 封装关系 ===
                    for (Object rObj : relList) {
                        if (rObj instanceof org.neo4j.driver.types.Relationship rel) {
                            long startId = rel.startNodeId();
                            long endId = rel.endNodeId();
                            String type = rel.type();
                            long relId = rel.id();

                            DynamicEntity startNode = entityMap.get(startId);
                            DynamicEntity endNode = entityMap.get(endId);
                            if (startNode == null || endNode == null) continue;

                            DynamicEntityRelationship relationship = new DynamicEntityRelationship();
                            relationship.setId(relId);
                            relationship.setEndNode(endNode);
                            relationship.setDirection("OUT");
                            startNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(relationship);

                            DynamicEntityRelationship reverse = new DynamicEntityRelationship();
                            reverse.setId(relId);
                            reverse.setEndNode(startNode);
                            reverse.setDirection("IN");
                            endNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(reverse);
                        }
                    }

                    rows.addAll(entityMap.values());
                });

        Long count = this.count(wrapper);
        return new PageResult<>(rows, count);
    }


    default PageResult<DynamicEntity> findPageWithLimitedRelations(Neo4jQueryWrapper<DynamicEntity> wrapper) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        String cypherQuery = wrapper.getCypherPageQuery();
        Map<String, Object> params = wrapper.getParams();
        List<DynamicEntity> rows = new ArrayList<>();

        neo4jClient.query(cypherQuery)
                .bindAll(params)
                .fetch()
                .all()
                .forEach(record -> {

                    // 1️⃣ 获取 nodes 与 relationships
                    List<Object> nodeList = (List<Object>) record.get("nodes");
                    List<Object> relList = (List<Object>) record.get("relationships");

                    if (nodeList == null || nodeList.isEmpty()) return;

                    // 2️⃣ 将 nodes 封装成 Map<id, DynamicEntity>
                    Map<Long, DynamicEntity> entityMap = new HashMap<>();
                    for (Object nObj : nodeList) {
                        if (nObj instanceof org.neo4j.driver.types.Node n) {
                            DynamicEntity entity = new DynamicEntity();
                            entity.setId(n.id());
                            entity.setName(n.asMap().getOrDefault("name", "").toString());
                            entity.setLabels(StreamSupport.stream(n.labels().spliterator(), false)
                                    .collect(Collectors.toSet()));
                            entity.setDynamicProperties(new HashMap<>(n.asMap()));
                            entity.setRelationshipEntityMap(new HashMap<>());
                            entityMap.put(n.id(), entity);
                        }
                    }

                    // 3️⃣ 遍历 relationships，建立映射
                    for (Object rObj : relList) {
                        if (rObj instanceof org.neo4j.driver.types.Relationship rel) {
                            long startId = rel.startNodeId();
                            long endId = rel.endNodeId();
                            String type = rel.type();
                            long relId = rel.id();

                            DynamicEntity startNode = entityMap.get(startId);
                            DynamicEntity endNode = entityMap.get(endId);
                            if (startNode == null || endNode == null) continue;

                            // 关系对象
                            DynamicEntityRelationship relationship = new DynamicEntityRelationship();
                            relationship.setId(relId);
                            relationship.setEndNode(endNode);
                            relationship.setDirection("OUT");

                            // 添加到 startNode
                            startNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(relationship);

                            // 对称反向关系（保持双向一致）
                            DynamicEntityRelationship reverse = new DynamicEntityRelationship();
                            reverse.setId(relId);
                            reverse.setEndNode(startNode);
                            reverse.setDirection("IN");

                            endNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(reverse);
                        }
                    }

                    // 4️⃣ 取出当前层的节点加入结果（分页层）
                    // 若想分页只返回前几个节点，可控制 size
                    rows.addAll(entityMap.values());
                });

        Long count = this.count(wrapper);
        return new PageResult<>(rows, count);
    }

    /*default PageResult<DynamicEntity> findPageWithLimitedRelations(
            Neo4jQueryWrapper<DynamicEntity> wrapper,
            int page,
            int size,
            int relationLimit,
            int maxLevel) {

        String baseMatch = wrapper.getCypherQuery();
        Map<String, Object> params = new HashMap<>(wrapper.getParams());
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);

        // === APOC 查询：分页 + 层级限制 + 关系限制 ===
        String cypherQuery = """
                    MATCH (n)
                    WHERE n.dynamicProperties_release_status = 0
                    AND n.dynamicProperties_task_id = 9
                    WITH n
                    ORDER BY id(n)
                    SKIP $skip LIMIT $limit
                    CALL apoc.path.subgraphAll(n, {
                      maxLevel: $maxLevel,
                      relationshipFilter: '>|<',   // ✅ 明确指定可走出边和入边
                      labelFilter: '',
                      bfs: true,
                      limit: $limit
                    })
                    YIELD nodes, relationships
                    RETURN nodes, relationships
    """;

        // === 参数绑定 ===
        params.put("skip", (page - 1) * size);
        params.put("limit", size);
        params.put("relationLimit", relationLimit);
        params.put("maxLevel", maxLevel);

        List<DynamicEntity> rows = new ArrayList<>();

        neo4jClient.query(cypherQuery)
                .bindAll(params)
                .fetch()
                .all()
                .forEach(record -> {

                    // 1️⃣ 获取 nodes 与 relationships
                    List<Object> nodeList = (List<Object>) record.get("nodes");
                    List<Object> relList = (List<Object>) record.get("relationships");

                    if (nodeList == null || nodeList.isEmpty()) return;

                    // 2️⃣ 将 nodes 封装成 Map<id, DynamicEntity>
                    Map<Long, DynamicEntity> entityMap = new HashMap<>();
                    for (Object nObj : nodeList) {
                        if (nObj instanceof org.neo4j.driver.types.Node n) {
                            DynamicEntity entity = new DynamicEntity();
                            entity.setId(n.id());
                            entity.setName(n.asMap().getOrDefault("name", "").toString());
                            entity.setLabels(StreamSupport.stream(n.labels().spliterator(), false)
                                    .collect(Collectors.toSet()));
                            entity.setDynamicProperties(new HashMap<>(n.asMap()));
                            entity.setRelationshipEntityMap(new HashMap<>());
                            entityMap.put(n.id(), entity);
                        }
                    }

                    // 3️⃣ 遍历 relationships，建立映射
                    for (Object rObj : relList) {
                        if (rObj instanceof org.neo4j.driver.types.Relationship rel) {
                            long startId = rel.startNodeId();
                            long endId = rel.endNodeId();
                            String type = rel.type();
                            long relId = rel.id();

                            DynamicEntity startNode = entityMap.get(startId);
                            DynamicEntity endNode = entityMap.get(endId);
                            if (startNode == null || endNode == null) continue;

                            // 关系对象
                            DynamicEntityRelationship relationship = new DynamicEntityRelationship();
                            relationship.setId(relId);
                            relationship.setEndNode(endNode);
                            relationship.setDirection("OUT");

                            // 添加到 startNode
                            startNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(relationship);

                            // 对称反向关系（保持双向一致）
                            DynamicEntityRelationship reverse = new DynamicEntityRelationship();
                            reverse.setId(relId);
                            reverse.setEndNode(startNode);
                            reverse.setDirection("IN");

                            endNode.getRelationshipEntityMap()
                                    .computeIfAbsent(type, k -> new ArrayList<>())
                                    .add(reverse);
                        }
                    }

                    // 4️⃣ 取出当前层的节点加入结果（分页层）
                    // 若想分页只返回前几个节点，可控制 size
                    rows.addAll(entityMap.values());
                });

        Long count = this.count(wrapper);
        return new PageResult<>(rows, count);
    }*/

}
