package com.insmess.knowledge.neo4j.repository;

import com.insmess.knowledge.neo4j.enums.Neo4jLabelEnum;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import com.insmess.knowledge.common.core.page.PageResult;
import com.insmess.knowledge.common.utils.spring.SpringUtils;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;
import com.insmess.knowledge.neo4j.wrapper.Neo4jBuildWrapper;
import com.insmess.knowledge.neo4j.wrapper.Neo4jQueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends Neo4jRepository<T, ID> {

    /**
     * 基于包装器生成的查询语句读取节点实体。
     */
    default List<T> find(Neo4jQueryWrapper<T> wrapper) {
        return neo4jTemplate().findAll(wrapper.getCypherQuery(), wrapper.getParams(), wrapper.getEntityClass());
    }

    /**
     * 分页查询，返回数据与总量。
     */
    default PageResult<T> findPage(Neo4jQueryWrapper<T> wrapper) {
        List<T> rows = neo4jTemplate().findAll(wrapper.getCypherQuery(), wrapper.getParams(), wrapper.getEntityClass());
        Long count = this.count(wrapper);
        return new PageResult<>(rows, count);
    }

    /**
     * 关系链查询，直接使用封装好的 Cypher。
     */
    default List<T> relationChain(Neo4jQueryWrapper<T> wrapper) {
        return neo4jTemplate().findAll(wrapper.getRelationChainCypherQuery(), wrapper.getParams(), wrapper.getEntityClass());
    }

    /**
     * 返回节点属性映射（包含 id），兼容不同别名的查询结果。
     */
    default List<Map<String, Object>> nodeListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Collection<Map<String, Object>> rows = neo4jClient().query(wrapper.getNodeQuery(labels))
                .bindAll(wrapper.getParams())
                .fetch()
                .all();

        return rows.stream()
                .map(BaseRepository::pickNode)
                .filter(Objects::nonNull)
                .map(BaseRepository::nodeProperties)
                .collect(Collectors.toList());
    }

    /**
     * 查询正向关系。
     */
    default List<Map<String, Object>> relListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Collection<Map<String, Object>> rows = neo4jClient().query(wrapper.getRelQuery(labels))
                .bindAll(wrapper.getParams())
                .fetch()
                .all();

        return rows.stream()
                .map(BaseRepository::relationshipRow)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    /**
     * 查询双向关系并去重。
     */
    default List<Map<String, Object>> relListMapAll(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        String forwardCypher = wrapper.getRelQuery(labels);
        Map<String, Object> params = wrapper.getParams();
        String reverseCypher = forwardCypher.replace("-[r]->", "<-[r]-");

        Collection<Map<String, Object>> forwardRows = neo4jClient().query(forwardCypher)
                .bindAll(params)
                .fetch()
                .all();
        Collection<Map<String, Object>> reverseRows = neo4jClient().query(reverseCypher)
                .bindAll(params)
                .fetch()
                .all();

        Set<String> seen = new HashSet<>();
        List<Map<String, Object>> relationships = new ArrayList<>();
        Stream.concat(forwardRows.stream(), reverseRows.stream())
                .map(BaseRepository::relationshipRowWithDirection)
                .flatMap(Optional::stream)
                .forEach(rel -> {
                    String key = relationKey(rel);
                    if (seen.add(key)) {
                        relationships.add(rel);
                    }
                });
        return relationships;
    }

    default void executeUpdate(Neo4jBuildWrapper<T> wrapper, String label, Map<String, Object> paramMap, Map<String, Object> updateMap) {
        executeUpdate(wrapper, label, paramMap, updateMap, null);
    }

    default void executeUpdate(Neo4jBuildWrapper<T> wrapper,
                               String label,
                               Map<String, Object> paramMap,
                               Map<String, Object> updateMap,
                               List<String> addLabels) {
        String query = wrapper.updateQuery(label, paramMap, updateMap, addLabels);
        Map<String, Object> params = Optional.ofNullable(updateMap).orElse(paramMap);
        neo4jClient().query(query).bindAll(params).run();
    }

    /**
     * 同时更新节点属性与 label。
     */
    default void executeUpdatePropertiesAndLabel(Neo4jBuildWrapper<T> wrapper,
                                                 String label,
                                                 Map<String, Object> paramMap,
                                                 Map<String, Object> updateMap,
                                                 List<String> addLabel,
                                                 List<String> removeLabel) {
        String query = wrapper.updateQuery(label, paramMap, updateMap, addLabel, removeLabel);
        Map<String, Object> params = Optional.ofNullable(updateMap).orElse(paramMap);
        neo4jClient().query(query).bindAll(params).run();
    }

    /**
     * 实体融合：迁移关系并合并属性。
     */
    @Transactional
    default void fusionNode(Long sourceEntityId,
                            Long targetEntityId,
                            Neo4jQueryWrapper<T> startQueryWrapper,
                            Neo4jBuildWrapper<T> wrapper,
                            Map<String, Object> attributeMap) {
        Set<String> labels = Collections.singleton(Neo4jLabelEnum.DYNAMICENTITY.getLabel());
        List<Map<String, Object>> relations = relListMapAll(startQueryWrapper, labels);
        for (Map<String, Object> relation : relations) {
            Long startId = Long.valueOf(relation.get("startId").toString());
            Long endId = Long.valueOf(relation.get("endId").toString());
            String relationType = relation.get("relationType").toString();
            if (startId.equals(sourceEntityId)) {
                mergeRelationship(Neo4jLabelEnum.DYNAMICENTITY.getLabel(), wrapper, targetEntityId, endId, relationType, new HashMap<>());
            } else {
                mergeRelationship(Neo4jLabelEnum.DYNAMICENTITY.getLabel(), wrapper, startId, targetEntityId, relationType, new HashMap<>());
            }
        }
        Map<String, Object> targetParams = new HashMap<>();
        targetParams.put("id", targetEntityId);
        executeUpdate(wrapper, Neo4jLabelEnum.DYNAMICENTITY.getLabel(), targetParams, attributeMap);

        Map<String, Object> fusionFlag = new HashMap<>();
        fusionFlag.put("fusion", 1);
        Map<String, Object> sourceParams = new HashMap<>();
        sourceParams.put("id", sourceEntityId);
        executeUpdate(wrapper, Neo4jLabelEnum.DYNAMICENTITY.getLabel(), sourceParams, fusionFlag);
    }

    /**
     * 如果节点存在则更新，不存在则创建；允许追加新 label。
     */
    @Transactional
    default List<T> mergeCreateNode(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        return mergeCreateNode(label, wrapper, mergeMap, attributeMap, null);
    }

    default List<T> mergeCreateNode(String label,
                                    Neo4jBuildWrapper<T> wrapper,
                                    Map<String, Object> mergeMap,
                                    Map<String, Object> attributeMap,
                                    String newLabel) {
        String cypherQuery = wrapper.mergeCreateNode(label, mergeMap, attributeMap, newLabel);
        Map<String, Object> params = normalizeParams(mergeMap, attributeMap);

        return new ArrayList<>(neo4jClient().query(cypherQuery)
                .bindAll(params)
                .fetchAs(wrapper.getEntityClass())
                .all());
    }

    /**
     * 创建或更新关系（节点属性由 Map 描述）。
     */
    @Transactional
    default List<T> mergeRelationship(String label,
                                      Neo4jBuildWrapper<T> wrapper,
                                      Map<String, Object> startNodeMap,
                                      Map<String, Object> endNodeMap,
                                      String rel,
                                      Map<String, Object> relMap) {
        String cypherQuery = wrapper.createRelationship(label, startNodeMap, endNodeMap, rel, relMap);
        Map<String, Object> params = mergeParams(startNodeMap, endNodeMap, relMap);
        return new ArrayList<>(neo4jClient().query(cypherQuery)
                .bindAll(params)
                .fetchAs(wrapper.getEntityClass())
                .all());
    }

    /**
     * 根据节点 id 创建关系。
     */
    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Long startNodeId, Long endNodeId, String rel, Map<String, Object> relMap) {
        String cypherQuery = wrapper.createRelationship(label, startNodeId, endNodeId, rel, relMap);
        return neo4jTemplate().findAll(cypherQuery, wrapper.getEntityClass());
    }

    /**
     * 统计查询。
     */
    default Long count(Neo4jQueryWrapper<T> wrapper) {
        return neo4jTemplate().count(wrapper.getCypherCountQuery(), wrapper.getParams());
    }

    /**
     * 根据节点 id 删除节点及其关系。
     */
    @Query("MATCH (n) WHERE id(n) = $nodeId DETACH DELETE n")
    void deleteNodeById(@Param("nodeId") Long nodeId);

    /**
     * 根据节点 id 集合删除节点及其关系。
     */
    @Query("MATCH (n) WHERE id(n) IN $nodeIds DETACH DELETE n")
    void deleteNodeByIds(@Param("nodeIds") List<Long> nodeIds);

    /**
     * 删除节点的指定属性。
     */
    default void deleteNodeAttributeById(Long nodeId, String attributeKey) {
        Neo4jBuildWrapper<DynamicEntity> wrapper = new Neo4jBuildWrapper<>(DynamicEntity.class);
        neo4jTemplate().findAll(wrapper.deleteNodeAttribute(nodeId, attributeKey), wrapper.getEntityClass());
    }

    /**
     * 根据关系 id 删除关系。
     */
    @Query("MATCH ()-[r]->() WHERE id(r) = $relationshipId DELETE r")
    void deleteRelationshipById(@Param("relationshipId") Long relationshipId);

    /**
     * 根据关系 id 集合删除关系。
     */
    @Query("MATCH ()-[r]->() WHERE id(r) IN $relationshipIds DELETE r")
    void deleteRelationshipsByIds(@Param("relationshipIds") List<Long> relationshipIds);

    private static Neo4jTemplate neo4jTemplate() {
        return SpringUtils.getBean(Neo4jTemplate.class);
    }

    private static Neo4jClient neo4jClient() {
        return SpringUtils.getBean(Neo4jClient.class);
    }

    private static Node pickNode(Map<String, Object> row) {
        Object node = row.get("n");
        if (node instanceof Node) {
            return (Node) node;
        }
        return row.values().stream()
                .filter(Node.class::isInstance)
                .map(Node.class::cast)
                .findFirst()
                .orElse(null);
    }

    private static Map<String, Object> nodeProperties(Node node) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", node.id());
        properties.putAll(node.asMap());
        return properties;
    }

    private static Optional<Map<String, Object>> relationshipRow(Map<String, Object> row) {
        Node start = (Node) row.get("n");
        Node end = (Node) row.get("m");
        Relationship rel = (Relationship) row.get("r");
        if (start == null || end == null || rel == null) {
            return Optional.empty();
        }
        return Optional.of(buildRelationshipMap(start, end, rel));
    }

    private static Optional<Map<String, Object>> relationshipRowWithDirection(Map<String, Object> row) {
        Node start = (Node) row.get("n");
        Node end = (Node) row.get("m");
        Relationship rel = (Relationship) row.get("r");
        if (start == null || end == null || rel == null) {
            return Optional.empty();
        }
        Map<String, Object> map = new HashMap<>();
        if (rel.startNodeId() == start.id()) {
            map.put("startId", start.id());
            map.put("startName", start.asMap().get("name"));
            map.put("endId", end.id());
            map.put("endName", end.asMap().get("name"));
        } else {
            map.put("startId", end.id());
            map.put("startName", end.asMap().get("name"));
            map.put("endId", start.id());
            map.put("endName", start.asMap().get("name"));
        }
        map.put("id", rel.id());
        map.put("relationType", rel.type());
        return Optional.of(map);
    }

    private static Map<String, Object> buildRelationshipMap(Node start, Node end, Relationship rel) {
        Map<String, Object> relationshipMap = new HashMap<>();
        relationshipMap.put("startId", start.id());
        relationshipMap.put("endId", end.id());
        relationshipMap.put("startName", start.asMap().get("name"));
        relationshipMap.put("endName", end.asMap().get("name"));
        relationshipMap.put("id", rel.id());
        relationshipMap.put("relationType", rel.type());
        return relationshipMap;
    }

    private static String relationKey(Map<String, Object> relMap) {
        long start = Long.parseLong(relMap.get("startId").toString());
        long end = Long.parseLong(relMap.get("endId").toString());
        String type = relMap.get("relationType").toString();
        long min = Math.min(start, end);
        long max = Math.max(start, end);
        return min + "_" + type + "_" + max;
    }

    @SafeVarargs
    private static Map<String, Object> mergeParams(Map<String, Object>... maps) {
        Map<String, Object> combined = new HashMap<>();
        for (Map<String, Object> map : maps) {
            if (map != null) {
                combined.putAll(map);
            }
        }
        return combined;
    }

    private static Map<String, Object> normalizeParams(Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        Map<String, Object> combined = mergeParams(mergeMap, attributeMap);
        combined.replaceAll((key, value) -> {
            if (value instanceof java.sql.Date date) {
                return date.toLocalDate();
            }
            if (value instanceof BigDecimal decimal) {
                return decimal.doubleValue();
            }
            if (value instanceof BigInteger integer) {
                return integer.longValue();
            }
            return value;
        });
        return combined;
    }
}
