package com.insmess.knowledge.module.graph.service.extract.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insmess.knowledge.common.database.DataSourceFactory;
import com.insmess.knowledge.common.database.DbQuery;
import com.insmess.knowledge.common.database.constants.DbQueryProperty;
import com.insmess.knowledge.module.graph.dao.po.extract.neo4j.GraphExtractionEntity;
import com.insmess.knowledge.module.graph.dao.po.extract.neo4j.GraphExtractionMergeDO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphAttributeMappingPO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphRelationMappingPO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaMappingPO;
import com.insmess.knowledge.module.graph.enums.ExtTaskStatus;
import com.insmess.knowledge.module.graph.enums.ReleaseStatus;
import com.insmess.knowledge.module.graph.repository.GraphNeo4jRepository;
import com.insmess.knowledge.module.graph.service.ontology.GraphAttributeMappingService;
import com.insmess.knowledge.module.graph.service.ontology.GraphRelationMappingService;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaMappingService;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingPageReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.struct.KnowbaseDatasourcePO;
import com.insmess.knowledge.module.knowbase.service.struct.KnowbaseDatasourceService;
import com.insmess.knowledge.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;
import com.insmess.knowledge.neo4j.enums.Neo4jLabelEnum;
import com.insmess.knowledge.neo4j.wrapper.Neo4jBuildWrapper;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.StringUtils;
import com.insmess.knowledge.common.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphStructTaskMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskService;
/**
 * 结构化抽取任务Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphStructTaskServiceImpl  extends ServiceImpl<GraphStructTaskMapper,GraphStructTaskPO> implements GraphStructTaskService {
    @Resource
    private GraphStructTaskMapper graphStructTaskMapper;

    @Resource
    private GraphNeo4jRepository graphNeo4jRepository;

    @Resource
    private KnowbaseDatasourceService knowbaseDatasourceService;

    @Resource
    private GraphSchemaMappingService graphSchemaMappingService;

    @Resource
    private GraphAttributeMappingService graphAttributeMappingService;

    @Resource
    private DataSourceFactory dataSourceFactory;
    @Autowired
    private GraphRelationMappingService graphRelationMappingService;

    @Override
    public Page<GraphStructTaskPO> pageGraphStructTask(GraphStructTaskPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphStructTaskPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphStructTaskPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphStructTaskMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphStructTask(GraphStructTaskSaveReqVO saveReqVO) {
        GraphStructTaskPO graphStructTaskPO = GraphStructTaskConvert.INSTANCE.convertToPO(saveReqVO);
        graphStructTaskMapper.insert(graphStructTaskPO);
        return graphStructTaskPO.getId();
    }

    @Override
    public int updateGraphStructTask(GraphStructTaskSaveReqVO updateReqVO) {
        GraphStructTaskPO updateObj = GraphStructTaskConvert.INSTANCE.convertToPO(updateReqVO);
        return graphStructTaskMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphStructTask(Collection<Long> idList) {
        return graphStructTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphStructTaskPO getGraphStructTaskById(Long id) {
        return graphStructTaskMapper.selectById(id);
    }

    @Override
    public List<GraphStructTaskPO> listGraphStructTask() {
        return graphStructTaskMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphStructTaskPO> mapGraphStructTask() {
        List<GraphStructTaskPO> graphStructTaskList = graphStructTaskMapper.selectList(null);
        return graphStructTaskList.stream()
                .collect(Collectors.toMap(
                        GraphStructTaskPO::getId,
                        graphStructTaskPO -> graphStructTaskPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入结构化抽取任务数据
         *
         * @param importExcelList 结构化抽取任务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphStructTask(List<GraphStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphStructTaskRespVO respVO : importExcelList) {
                try {
                    GraphStructTaskPO graphStructTaskPO = BeanUtils.toBean(respVO, GraphStructTaskPO.class);
                    Long graphStructTaskId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphStructTaskId != null) {
                            GraphStructTaskPO existingGraphStructTask = graphStructTaskMapper.selectById(graphStructTaskId);
                            if (existingGraphStructTask != null) {
                                graphStructTaskMapper.updateById(graphStructTaskPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphStructTaskId + " 的结构化抽取任务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphStructTaskId + " 的结构化抽取任务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphStructTaskPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphStructTaskId);
                        GraphStructTaskPO existingGraphStructTask = graphStructTaskMapper.selectOne(queryWrapper);
                        if (existingGraphStructTask == null) {
                            graphStructTaskMapper.insert(graphStructTaskPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphStructTaskId + " 的结构化抽取任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphStructTaskId + " 的结构化抽取任务记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }

    /**
     * 执行结构化抽取任务
     *
     * @param taskId 结构化抽取任务编号
     */
    @Override
    public void executeExtract(Long taskId) {
        //根据任务id查询任务
        GraphStructTaskPO graphStructTaskPO = getById(taskId);
        //已经发布的任务不能再进行抽取了
        if (ReleaseStatus.PUBLISHED.getValue().equals(graphStructTaskPO.getPublishStatus())) {
            throw new RuntimeException("任务已发布，不能重新抽取");
        }
        //设置任务执行状态
        graphStructTaskPO.setStatus(ExtTaskStatus.INPROGRESS.getValue());
        //执行任务
        new Thread(() -> {
            try {
                this.execute(graphStructTaskPO);
                graphStructTaskPO.setStatus(ExtTaskStatus.EXECUTED.getValue());
                graphStructTaskMapper.updateById(graphStructTaskPO);
            } catch (Exception e) {
                log.error("任务执行异常", e);
            }
        }).start();
    }

    private void execute(GraphStructTaskPO graphStructTaskPO) throws JsonProcessingException {
        //删除该任务所有的数据
        graphNeo4jRepository.deleteByTaskId(graphStructTaskPO.getId());
        //数据源信息
        KnowbaseDatasourcePO datasource = knowbaseDatasourceService.getById(graphStructTaskPO.getDatasourceId());
        //根据任务id获取概念
        List<GraphSchemaMappingPO> schemaMappingList = graphSchemaMappingService.listByTaskId(graphStructTaskPO.getId());
        //根据每个映射的概念抽取对应内容
        for (GraphSchemaMappingPO schemaMappingPO : schemaMappingList) {
            //获取表名
            String tableName = schemaMappingPO.getTableName();
            //获取数据源信息
            DbQueryProperty dbQueryProperty = new DbQueryProperty(
                    datasource.getDatasourceType(),
                    datasource.getHost(),
                    datasource.getUsername(),
                    datasource.getPassword(),
                    datasource.getPort().intValue(),
                    datasource.getSchemaName(),
                    datasource.getSid()
            );
            //创建数据库查询对象
            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            //查询该表数据
            List<Map<String, Object>> mapList = dbQuery.queryList("SELECT * FROM " + tableName);
            //查询所有相关属性映射
            GraphAttributeMappingPageReqVO attributeMappingReqVo = new GraphAttributeMappingPageReqVO();
            attributeMappingReqVo.setTaskId(graphStructTaskPO.getId());
            attributeMappingReqVo.setTableName(tableName);
            List<GraphAttributeMappingPO> attributeMappingList = graphAttributeMappingService.list(attributeMappingReqVo);
            //将属性和表字段对应上, 存成一个数组
            ArrayList<ConcurrentHashMap<String, Object>> maps = new ArrayList<>();
            for (Map<String, Object> objectMap : mapList) {
                //创建属性map  以属性id为key,字段值为value
                ConcurrentHashMap<String, Object> nodes = new ConcurrentHashMap<>();
                nodes.put("task_id", graphStructTaskPO.getId());
                nodes.put("database_id", datasource.getId());
                nodes.put("table_name", tableName);
                nodes.put("schema_id", schemaMappingPO.getSchemaId());
                nodes.put("schema_name", schemaMappingPO.getSchemaName());
                nodes.put("release_status", ReleaseStatus.UNPUBLISHED.getValue());
                for (ConcurrentHashMap.Entry<String, Object> entry : objectMap.entrySet()) {
                    // 获取字段名
                    String columnName = entry.getKey();
                    // 获取字段值
                    Object columnValue = entry.getValue();
                    //如果这个字段是实体名称, 这个字段在前端选择
                    if (columnName.equals(schemaMappingPO.getEntityNameField())) {
                        nodes.put("name", columnValue);
                    }
                    //每个表必须要有id字段
                    if ("id".equals(columnName)) {
                        nodes.put("data_id", columnValue);
                    }
                    for (GraphAttributeMappingPO attributeMappingDO : attributeMappingList) {
                        //如果这个字段映射过属性, 就把属性放到节点map中
                        if (attributeMappingDO.getAttributeId() != null && columnName.equals(attributeMappingDO.getFieldName())) {
                            //把所有添加属性映射的区分出来, 方便展示
                            nodes.put("attribute_id_" + attributeMappingDO.getAttributeId(), columnValue);
                        }
                    }
                }
                maps.add(nodes);
            }
            log.info("-----节点maps---:{}", maps);
            //存储所有抽取出来的节点
            for (ConcurrentHashMap<String, Object> map : maps) {
                // 创建头部节点并动态添加属性
                Neo4jBuildWrapper<GraphExtractionEntity> wrapper = new Neo4jBuildWrapper<>(GraphExtractionEntity.class);
                GraphExtractionMergeDO.Node extractionMergeDO = new GraphExtractionMergeDO.Node();
                extractionMergeDO.setName(map.get("name").toString());
                extractionMergeDO.setTask_id(graphStructTaskPO.getId());
                extractionMergeDO.setTable_name(tableName);
                extractionMergeDO.setData_id(Long.valueOf(map.get("data_id").toString()));
                extractionMergeDO.setDatabase_id(Long.valueOf(datasource.getId().toString()));
                Map<String, Object> mergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(extractionMergeDO), Map.class);
                String label = Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.STRUCTURED.getLabel();
                graphNeo4jRepository.mergeCreateNode(label, wrapper, mergeMap, map, schemaMappingPO.getSchemaName());
            }
            //查询映射过的关系
            GraphRelationMappingPageReqVO relationMappingPageReqVO = new GraphRelationMappingPageReqVO();
            relationMappingPageReqVO.setTaskId(graphStructTaskPO.getId());
            relationMappingPageReqVO.setTableName(tableName);
            List<GraphRelationMappingPO> relationMappingList = graphRelationMappingService.list(relationMappingPageReqVO);
            //循环映射的关系
            for (GraphRelationMappingPO relationMappingDO : relationMappingList) {
                //字段名
                String fieldName = relationMappingDO.getFieldName();
                //关联表
                String relationTable = relationMappingDO.getRelationTable();
                //关联表字段
                String relationField = relationMappingDO.getRelationField();
                // 内连接连表, 查询关系映射
                String query2 = "SELECT a.*,b.* FROM " + tableName + " a INNER JOIN " + relationTable + " b ON a." + fieldName + " = b." + relationField + ";";
                log.info("查询sql:{}", query2);
                List<Map<String, Object>> mapList2 = dbQuery.queryList(query2);
                log.info("----关系-------:{}", mapList2);
                //存储节点和关系, 没有关系的节点也存储到neo4j
                for (Map<String, Object> map : maps) {
                    // 创建头部节点并动态添加属性
                    List<Map<String, Object>> filteredList = mapList2.stream()
                            .filter(m -> m.containsKey("a_id") && m.get("a_id").equals(map.get("data_id")))
                            .collect(Collectors.toList());
                    for (Map<String, Object> relMap : filteredList) {
                        boolean status = false;
                        //实体的属性 循环匹配是否有对应的关系 //尾部实体
                        Map<String, Object> endMap = new ConcurrentHashMap<>();
                        Iterator<Map.Entry<String, Object>> iterator = relMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            ConcurrentHashMap.Entry<String, Object> relEntry = iterator.next();
                            // 修改或者删除元素
                            if (relEntry.getKey().startsWith("a_")) {
                                iterator.remove();  // 使用 iterator 的 remove() 方法
                            }
                            //如果是实体id字段, 匹配节点map中的值, 判断是否是同一个, 如果是同一个的话, 判断是否映射的有关系
                            //根据节点map中的节点data_id和关系映射中的a_id匹配
                            if ("a_id".equals(relEntry.getKey())) {
                                //如果是同一个实体 并且b_表的数据不为空, 说明有对应关系存在, 需要存储关系
                                if (map.get("data_id").equals(relEntry.getValue())) {
                                    relMap.keySet().removeIf(key -> key.startsWith("a_"));
                                    // 修改以 "b_" 开头的键，去掉 "b_" 前缀
                                    Set<String> keysToModify = new HashSet<>();
                                    for (String key : relMap.keySet()) {
                                        if (key.startsWith("b_")) {
                                            keysToModify.add(key);
                                        }
                                    }
                                    // 更新键，去掉 "b_" 前缀
                                    for (String key : keysToModify) {
                                        Object value = relMap.remove(key);
                                        // 去掉 "b_" 前缀
                                        relMap.put(key.substring(2), value);
                                    }
                                    endMap = relMap;
                                    status = true;
                                }
                            }
                        }
                        //如果有映射的关系, 添加尾部节点和关系
                        if (status) {
                            // 创建尾部节点并动态添加属性
                            endMap.put("task_id", graphStructTaskPO.getId());
                            endMap.put("table_name", relationTable);
                            endMap.put("data_id", endMap.get("id"));
                            endMap.put("database_id", datasource.getId());
                            endMap.put("release_status", ReleaseStatus.UNPUBLISHED.getValue());
                            endMap.remove("id");
                            Neo4jBuildWrapper<GraphExtractionEntity> build = new Neo4jBuildWrapper<>(GraphExtractionEntity.class);
                            GraphExtractionMergeDO.Node extractionMergeDO = new GraphExtractionMergeDO.Node();
                            extractionMergeDO.setName(endMap.get(relationMappingDO.getRelationNameField()).toString());
                            extractionMergeDO.setTask_id(Long.valueOf(graphStructTaskPO.getId().toString()));
                            extractionMergeDO.setTable_name(relationTable);
                            extractionMergeDO.setData_id(Long.valueOf(endMap.get("data_id").toString()));
                            extractionMergeDO.setDatabase_id(Long.valueOf(datasource.getId().toString()));
                            ConcurrentHashMap<String, Object> endMergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(extractionMergeDO), ConcurrentHashMap.class);
                            String label = Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.STRUCTURED.getLabel();
                            graphNeo4jRepository.mergeCreateNode(label, build, endMergeMap, endMap);
                            //创建关系
                            //起点
                            GraphExtractionMergeDO.CreateRelationshipNode startNode = new GraphExtractionMergeDO.CreateRelationshipNode();
                            startNode.setData_id(Long.valueOf(map.get("data_id").toString()));
                            startNode.setTask_id(Long.valueOf(graphStructTaskPO.getId().toString()));
                            startNode.setTable_name(tableName);
                            ConcurrentHashMap<String, Object> startNodeMap = new ObjectMapper().readValue(JSONObject.toJSONString(startNode), ConcurrentHashMap.class);
                            //结点
                            GraphExtractionMergeDO.CreateRelationshipNode endNode = new GraphExtractionMergeDO.CreateRelationshipNode();
                            endNode.setData_id(Long.valueOf(endMap.get("data_id").toString()));
                            endNode.setTask_id(Long.valueOf(graphStructTaskPO.getId().toString()));
                            endNode.setTable_name(relationTable);
                            ConcurrentHashMap<String, Object> endNodeMap = new ObjectMapper().readValue(JSONObject.toJSONString(endNode), ConcurrentHashMap.class);
                            String rel = relationMappingDO.getRelation();
                            //关系
                            ConcurrentHashMap<String, Object> relMa = new ConcurrentHashMap<>();
                            relMa.put("task_id", graphStructTaskPO.getId());
                            graphNeo4jRepository.mergeRelationship(Neo4jLabelEnum.STRUCTURED.getLabel(), build, startNodeMap, endNodeMap, rel, relMa);
                        }
                    }
                }
            }
        }

    }



    private LambdaQueryWrapperX<GraphStructTaskPO> queryCondition(GraphStructTaskPageReqVO pageReqVO) {
        GraphStructTaskPO graphStructTaskPO = GraphStructTaskConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphStructTaskPO>()
                .eqIfPresent(GraphStructTaskPO::getKnowledgeId, graphStructTaskPO.getKnowledgeId())
                .likeIfPresent(GraphStructTaskPO::getName, graphStructTaskPO.getName())
                .eqIfPresent(GraphStructTaskPO::getStatus, graphStructTaskPO.getStatus())
                .eqIfPresent(GraphStructTaskPO::getPublishStatus, graphStructTaskPO.getPublishStatus())
                .eqIfPresent(GraphStructTaskPO::getPublishTime, graphStructTaskPO.getPublishTime())
                .eqIfPresent(GraphStructTaskPO::getPublisherId, graphStructTaskPO.getPublisherId())
                .eqIfPresent(GraphStructTaskPO::getPublishBy, graphStructTaskPO.getPublishBy())
                .eqIfPresent(GraphStructTaskPO::getDatasourceId, graphStructTaskPO.getDatasourceId())
                .likeIfPresent(GraphStructTaskPO::getDatasourceName, graphStructTaskPO.getDatasourceName())
                .eqIfPresent(GraphStructTaskPO::getCreateTime, graphStructTaskPO.getCreateTime())
                .orderByAsc(GraphStructTaskPO::getCreateTime);
    }

}
