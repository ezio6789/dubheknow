package com.insmess.knowledge.neo4j.repository;

import com.insmess.knowledge.neo4j.enums.Neo4jLabelEnum;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.NodeValue;
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

@NoRepositoryBean  // 关键注解：阻止Spring实例化此基础仓库
public interface BaseRepository<T, ID> extends Neo4jRepository<T, ID> {

    /**
     * 根据查询条件查询
     *
     * @param wrapper
     * @return
     */
    default List<T> find(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getCypherQuery();
        Map<String, Object> params = wrapper.getParams();
        List<T> all = neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
        return all;
    }


    /**
     * 分页查询
     *
     * @param wrapper
     * @return
     */
    default PageResult<T> findPage(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getCypherQuery();
        Map<String, Object> params = wrapper.getParams();
        List<T> row = neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
        Long count = this.count(wrapper);
        return new PageResult<>(row, count);
    }




    default List<T> relationChain(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getRelationChainCypherQuery();
        Map<String, Object> params = wrapper.getParams();
         return neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
    }

    /**
     * 获取节点list, 所有属性
     *
     * @param wrapper
     * @return
     */
    default List<Map<String, Object>> nodeListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        List<Map<String, Object>> collect = neo4jClient.query(wrapper.getNodeQuery(labels))
                .bindAll(wrapper.getParams())
                .fetchAs(NodeValue.class)  // 获取 Node 对象
                .all()
                .stream()
                .map(nodeValue -> {
                    HashMap<String, Object> properties = new HashMap<>();
                    InternalNode node = (InternalNode) nodeValue.asNode();
                    properties.put("id", node.id());
                    // 获取节点的属性
                    node.asMap().forEach((key, value) -> properties.put(key, value));
                    return properties;
                })
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 根据条件查询关系
     *
     * @param wrapper
     * @return
     */
    default List<Map<String, Object>> relListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        Collection<Map<String, Object>> result = neo4jClient.query(wrapper.getRelQuery(labels))
                .bindAll(wrapper.getParams())
                .fetch()  // 删除 fetchAs(Record.class)，直接使用默认映射
                .all();
        ArrayList<Map<String, Object>> relationships = new ArrayList<>();
        for (Map<String, Object> objectMap : result) {
            InternalNode n = (InternalNode) objectMap.get("n");
            InternalNode m = (InternalNode) objectMap.get("m");
            Relationship r = (Relationship) objectMap.get("r");

            if (m != null) {
                Map<String, Object> map = m.asMap();
                HashMap<String, Object> relationshipMap = new HashMap<>();
                relationshipMap.put("startId", n.id());
                relationshipMap.put("endId", m.id());
                relationshipMap.put("startName", n.asMap().get("name"));
                relationshipMap.put("endName", map.get("name"));
                relationshipMap.put("id", r.id());
                relationshipMap.put("relationType", r.type());
                relationships.add(relationshipMap);
            }
        }
        return relationships;
    }

    /**
     * 根据条件查询关系（所有关系，包含出关系和入关系）
     *
     * @param wrapper
     * @return
     */
    default List<Map<String, Object>> relListMapAll(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        // 分别查询正向和反向关系
        String cypherQuery = wrapper.getRelQuery(labels);
        Map<String, Object> params = wrapper.getParams();

        // 查询正向关系
        Collection<Map<String, Object>> forwardResult = neo4jClient.query(cypherQuery)
                .bindAll(params)
                .fetch()
                .all();

        // 查询反向关系（修改Cypher语句）
        String reverseCypherQuery = cypherQuery.replace("-[r]->", "<-[r]-");
        Collection<Map<String, Object>> reverseResult = neo4jClient.query(reverseCypherQuery)
                .bindAll(params)
                .fetch()
                .all();

        // 合并结果
        List<Map<String, Object>> allResults = new ArrayList<>();
        allResults.addAll(forwardResult);
        allResults.addAll(reverseResult);

        ArrayList<Map<String, Object>> relationships = new ArrayList<>();
        Set<String> processedRelations = new HashSet<>();

        for (Map<String, Object> objectMap : allResults) {
            InternalNode n = (InternalNode) objectMap.get("n");
            InternalNode m = (InternalNode) objectMap.get("m");
            Relationship r = (Relationship) objectMap.get("r");

            if (m != null) {
                // 创建统一的关系标识符
                String relationKey = Math.min(n.id(), m.id()) + "_" + r.type() + "_" + Math.max(n.id(), m.id());

                if (processedRelations.contains(relationKey)) {
                    continue;
                }

                processedRelations.add(relationKey);
                Map<String, Object> map = m.asMap();
                HashMap<String, Object> relationshipMap = new HashMap<>();
                //判断方向
                if (r.startNodeId() == n.id()) {
                    relationshipMap.put("startId", n.id());
                    relationshipMap.put("startName", n.asMap().get("name"));
                    relationshipMap.put("endId", m.id());
                    relationshipMap.put("endName", map.get("name"));
                } else {
                    relationshipMap.put("startId", m.id());
                    relationshipMap.put("startName", map.get("name"));
                    relationshipMap.put("endId", n.id());
                    relationshipMap.put("endName", n.asMap().get("name"));
                }
                relationshipMap.put("id", r.id());
                relationshipMap.put("relationType", r.type());
                relationships.add(relationshipMap);
            }
        }
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
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        String query = wrapper.updateQuery(label, paramMap, updateMap, addLabels);

        neo4jClient.query(query)
                .bindAll(updateMap != null ? updateMap : paramMap)
                .run();
    }


    /**
     * 更新节点的属性和标签
     *
     * @param wrapper
     * @return
     */
    default void executeUpdatePropertiesAndLabel(Neo4jBuildWrapper<T> wrapper,
                                                 String label,
                                                 Map<String, Object> paramMap,
                                                 Map<String, Object> updateMap,
                                                 List<String> addLabel,
                                                 List<String> removeLabel) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        String query = wrapper.updateQuery(label, paramMap, updateMap, addLabel, removeLabel);

        neo4jClient.query(query)
                .bindAll(updateMap != null ? updateMap : paramMap)
                .run();
    }


    /**
     * 进行实体融合 将sourceEntityId合并到targetEntityId实体中
     * @param sourceEntityId 来源id
     * @param targetEntityId
     * @param startQueryWrapper 被合并节点的查询条件
     * @param attributeMap 合并后的属性
     * @return
     */
    @Transactional
    default void fusionNode(Long sourceEntityId, Long targetEntityId, Neo4jQueryWrapper<T> startQueryWrapper, Neo4jBuildWrapper<T> wrapper, Map<String, Object> attributeMap) {
        //融合实体
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        //融合关系
        //Neo4jLabelEnum.DYNAMICENTITY转成Set集合
        Set<String> labels = Collections.singleton(Neo4jLabelEnum.DYNAMICENTITY.getLabel());
        List<Map<String, Object>> maps = relListMapAll(startQueryWrapper, labels);
        for (Map<String, Object> map : maps) {
            Long startId = Long.valueOf(map.get("startId").toString());
            Long endId = Long.valueOf(map.get("endId").toString());
            String relationType = map.get("relationType").toString();
            Map<String, Object> relMap = new HashMap<>();
            //判断关系
            if (startId.equals(sourceEntityId)) {
                //2. 迁移关系。
                mergeRelationship(Neo4jLabelEnum.DYNAMICENTITY.getLabel(), wrapper, targetEntityId, endId, relationType, relMap);
            } else {
                //2. 迁移关系。
                mergeRelationship(Neo4jLabelEnum.DYNAMICENTITY.getLabel(), wrapper, startId, targetEntityId, relationType, relMap);
            }
        }
        //修改目标实体的属性
        Map<String, Object> params = new HashMap<>();
        params.put("id", targetEntityId);
        executeUpdate(wrapper, Neo4jLabelEnum.DYNAMICENTITY.getLabel(), params, attributeMap);
        //将原节点变为已融合状态
        Map<String, Object> attrMap2 = new HashMap<>();
        Map<String, Object> params2 = new HashMap<>();
        attrMap2.put("fusion", 1);
        params2.put("id", sourceEntityId);
        executeUpdate(wrapper, Neo4jLabelEnum.DYNAMICENTITY.getLabel(), params2, attrMap2);
    }

    /**
     * 如果节点存在, 更新此节点的属性, 如果节点不存在, 创建节点和属性
     *
     * @param wrapper
     * @param mergeMap
     * @param attributeMap
     * @return
     */
    @Transactional
    default List<T> mergeCreateNode(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        return mergeCreateNode(label, wrapper, mergeMap, attributeMap, null);
    }

    default List<T> mergeCreateNode(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> mergeMap, Map<String, Object> attributeMap, String newLabel) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        String cypherQuery = wrapper.mergeCreateNode(label, mergeMap, attributeMap, newLabel);

        // 合并两个Map，避免参数名冲突
        Map<String, Object> combinedParams = new HashMap<>();
        if (mergeMap != null) {
            combinedParams.putAll(mergeMap);
        }
        if (attributeMap != null) {
            combinedParams.putAll(attributeMap);
        }

        // 在传递参数之前进行转换
        if (combinedParams != null) {
            combinedParams.replaceAll((key, value) -> {
                if (value instanceof java.sql.Date) {
                    return ((java.sql.Date) value).toLocalDate();
                }
                if (value instanceof BigDecimal) {
                    return ((BigDecimal) value).doubleValue();
                }
                if (value instanceof BigInteger) {
                    return ((BigInteger) value).longValue();
                }
                return value;
            });
        }

        return new ArrayList<>(neo4jClient.query(cypherQuery)
                .bindAll(combinedParams)
                .fetchAs(wrapper.getEntityClass())
                .all());
    }


    /*default List<T> mergeCreateNode(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.mergeCreateNode(label, mergeMap, attributeMap);
        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
    }*/


    /**
     * 根据起点和结点创建关系 如果起点或者终点不存在, 会创建节点
     *
     * @param wrapper
     * @param startNodeMap
     * @param endNodeMap
     * @param rel
     * @return
     */
    @Transactional
    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> startNodeMap, Map<String, Object> endNodeMap, String rel, Map<String, Object> relMap) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        String cypherQuery = wrapper.createRelationship(label, startNodeMap, endNodeMap, rel, relMap);

        // 合并所有参数Map，避免参数名冲突
        Map<String, Object> combinedParams = new HashMap<>();
        if (startNodeMap != null) {
            combinedParams.putAll(startNodeMap);
        }
        if (endNodeMap != null) {
            combinedParams.putAll(endNodeMap);
        }
        if (relMap != null) {
            combinedParams.putAll(relMap);
        }

        return new ArrayList<>(neo4jClient.query(cypherQuery)
                .bindAll(combinedParams)
                .fetchAs(wrapper.getEntityClass())
                .all());
    }

//    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> startNodeMap, Map<String, Object> endNodeMap, String rel, Map<String, Object> relMap) {
//        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
//        String cypherQuery = wrapper.createRelationship(label, startNodeMap, endNodeMap, rel, relMap);
//        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
//    }

    /**
     * 根据起点id和结点id创建关系
     * @param label
     * @param wrapper
     * @param startNodeId
     * @param endNodeId
     * @param rel
     * @param relMap
     * @return
     */
    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Long startNodeId, Long endNodeId, String rel, Map<String, Object> relMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.createRelationship(label, startNodeId, endNodeId, rel, relMap);
        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
    }

    /**
     * 统计查询
     *
     * @param wrapper
     * @return
     */
    default Long count(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        return neo4jTemplate.count(wrapper.getCypherCountQuery(), wrapper.getParams());
    }

    /**
     * 根据节点id删除节点和相关关系
     *
     * @param nodeId
     */
    @Query("MATCH (n) WHERE id(n) = $nodeId DETACH DELETE n")
    void deleteNodeById(@Param("nodeId") Long nodeId);

    /**
     * 根据节点id删除节点和相关关系
     *
     * @param nodeIds
     */
    @Query("MATCH (n) WHERE id(n) IN $nodeIds DETACH DELETE n")
    void deleteNodeByIds(@Param("nodeIds") List<Long> nodeIds);

    /**
     * 根据节点id和属性的id删除属性
     *
     */
//    @Query("MATCH (n) WHERE id(n) = $nodeId REMOVE n.$attributeKey RETURN n")
//    void deleteNodeAttributeById(@Param("nodeId") Long nodeId, @Param("attributeKey") String attributeKey);
    default void deleteNodeAttributeById(Long nodeId, String attributeKey){
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        Neo4jBuildWrapper<DynamicEntity> wrapper = new Neo4jBuildWrapper<>(DynamicEntity.class);
        neo4jTemplate.findAll(wrapper.deleteNodeAttribute(nodeId,attributeKey), wrapper.getEntityClass());
    }

    /**
     * 根据关系id删除关系
     *
     * @param relationshipId
     */
    @Query("MATCH ()-[r]->() WHERE id(r) = $relationshipId DELETE r")
    void deleteRelationshipById(@Param("relationshipId") Long relationshipId);

    /**
     * 根据关系id集合删除关系
     *
     * @param relationshipIds
     */
    @Query("MATCH ()-[r]->() WHERE id(r) IN $relationshipIds DELETE r")
    void deleteRelationshipsByIds(@Param("relationshipIds") List<Long> relationshipIds);


}
