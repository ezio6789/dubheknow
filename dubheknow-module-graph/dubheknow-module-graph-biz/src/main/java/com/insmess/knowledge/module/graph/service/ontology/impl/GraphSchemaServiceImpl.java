package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaRelationPO;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaRelationService;
import com.insmess.knowledge.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import com.insmess.knowledge.common.utils.object.BeanUtils;
import com.insmess.knowledge.common.utils.StringUtils;
import com.insmess.knowledge.common.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaPO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphSchemaMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaService;
/**
 * 概念配置Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphSchemaServiceImpl  extends ServiceImpl<GraphSchemaMapper,GraphSchemaPO> implements GraphSchemaService {
    @Resource
    private GraphSchemaMapper graphSchemaMapper;

    @Resource
    private GraphSchemaRelationService graphSchemaRelationService;


    @Override
    public List<GraphSchemaPO> listExtSchemaTree(GraphSchemaPageReqVO extSchema) {
        return getExtSchemaListByPid(0L, extSchema.getKnowledgeId());
    }

    private List<GraphSchemaPO> getExtSchemaListByPid(Long pid, Long knowledgeId) {
        //根据pid获取
        List<GraphSchemaPO> extSchemaDOList = graphSchemaMapper.selectList(
                new QueryWrapper<GraphSchemaPO>().lambda()
                        .eq(GraphSchemaPO::getPid, pid)
                        .eq(GraphSchemaPO::getKnowledgeId, knowledgeId)
        );
        //extSchemaDOList中的每个子内容
        for (GraphSchemaPO extSchemaDO : extSchemaDOList) {
            extSchemaDO.setChildren(getExtSchemaListByPid(extSchemaDO.getId(), knowledgeId));
        }
        return extSchemaDOList;
    }

    @Override
    public List<GraphSchemaPO> getParentListByPid(Long id) {
        GraphSchemaPO extSchemaDO = graphSchemaMapper.selectById(id);
        //如果是空
        if (extSchemaDO == null) {
            return new ArrayList<>();
        }
        List<GraphSchemaPO> parentList = new ArrayList<>();
        parentList.add(extSchemaDO);
        //如果不是根节点
        if (!extSchemaDO.getPid().equals(0L)) {
            List<GraphSchemaPO> resultList = getParentListByPid(extSchemaDO.getPid());
            parentList.addAll(resultList);
        }
        return parentList;
    }

    @Override
    public Map<String, Object> getGraphOntology(Long knowledgeId) {
        //定义map集合，保存数据
        Map<String, Object> map = new HashMap<>();
        //查询该知识图谱下所有的本体（列表）
        List<GraphSchemaPO> extSchemaList = list(
                new QueryWrapper<GraphSchemaPO>().lambda().eq(GraphSchemaPO::getKnowledgeId, knowledgeId)
        );
        map.put("entities", extSchemaList);
        //查询该知识图谱下所有的关系（列表）
        List<GraphSchemaRelationPO> extSchemaRelationList = graphSchemaRelationService.list(
                new QueryWrapper<GraphSchemaRelationPO>().lambda().eq(GraphSchemaRelationPO::getKnowledgeId, knowledgeId)
        );
        //封装关系
        List<Map<String, Object>> extSchemaRelationListMap = new ArrayList<>();
        for (GraphSchemaRelationPO schemaRelationPO : extSchemaRelationList) {
            Map<String, Object> map2 = new HashMap<>();
            //查询起点和终点实体名称
            map2.put("startId", schemaRelationPO.getStartSchemaId());
            map2.put("endId", schemaRelationPO.getEndSchemaId());
            GraphSchemaPO startSchema = graphSchemaMapper.selectById(schemaRelationPO.getStartSchemaId());
            GraphSchemaPO endSchema = graphSchemaMapper.selectById(schemaRelationPO.getEndSchemaId());
            map2.put("startName", startSchema.getName());
            map2.put("endName", endSchema.getName());
            map2.put("id", schemaRelationPO.getId());
            map2.put("relationType", schemaRelationPO.getRelation());
            extSchemaRelationListMap.add(map2);
        }
        map.put("relationships", extSchemaRelationListMap);
        return map;
    }

    @Override
    public Page<GraphSchemaPO> pageGraphSchema(GraphSchemaPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphSchemaPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphSchemaPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphSchemaMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphSchema(GraphSchemaSaveReqVO saveReqVO) {
        GraphSchemaPO graphSchemaPO = GraphSchemaConvert.INSTANCE.convertToPO(saveReqVO);
        graphSchemaMapper.insert(graphSchemaPO);
        return graphSchemaPO.getId();
    }

    @Override
    public int updateGraphSchema(GraphSchemaSaveReqVO updateReqVO) {
        GraphSchemaPO updateObj = GraphSchemaConvert.INSTANCE.convertToPO(updateReqVO);
        return graphSchemaMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphSchema(Collection<Long> idList) {
        return graphSchemaMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphSchemaPO getGraphSchemaById(Long id) {
        return graphSchemaMapper.selectById(id);
    }

    @Override
    public List<GraphSchemaPO> listGraphSchema() {
        return graphSchemaMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphSchemaPO> mapGraphSchema() {
        List<GraphSchemaPO> graphSchemaList = graphSchemaMapper.selectList(null);
        return graphSchemaList.stream()
                .collect(Collectors.toMap(
                        GraphSchemaPO::getId,
                        graphSchemaPO -> graphSchemaPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入概念配置数据
         *
         * @param importExcelList 概念配置数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphSchema(List<GraphSchemaRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphSchemaRespVO respVO : importExcelList) {
                try {
                    GraphSchemaPO graphSchemaPO = BeanUtils.toBean(respVO, GraphSchemaPO.class);
                    Long graphSchemaId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphSchemaId != null) {
                            GraphSchemaPO existingGraphSchema = graphSchemaMapper.selectById(graphSchemaId);
                            if (existingGraphSchema != null) {
                                graphSchemaMapper.updateById(graphSchemaPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphSchemaId + " 的概念配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphSchemaId + " 的概念配置记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphSchemaPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphSchemaId);
                        GraphSchemaPO existingGraphSchema = graphSchemaMapper.selectOne(queryWrapper);
                        if (existingGraphSchema == null) {
                            graphSchemaMapper.insert(graphSchemaPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphSchemaId + " 的概念配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphSchemaId + " 的概念配置记录已存在。");
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

    private LambdaQueryWrapperX<GraphSchemaPO> queryCondition(GraphSchemaPageReqVO pageReqVO) {
        GraphSchemaPO graphSchemaPO = GraphSchemaConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphSchemaPO>()
                .likeIfPresent(GraphSchemaPO::getName, graphSchemaPO.getName())
                .eqIfPresent(GraphSchemaPO::getDescription, graphSchemaPO.getDescription())
                .eqIfPresent(GraphSchemaPO::getColor, graphSchemaPO.getColor())
                .eqIfPresent(GraphSchemaPO::getCreateTime, graphSchemaPO.getCreateTime())
                .eqIfPresent(GraphSchemaPO::getKnowledgeId, graphSchemaPO.getKnowledgeId())
                .eqIfPresent(GraphSchemaPO::getPid, graphSchemaPO.getPid())
                .orderByAsc(GraphSchemaPO::getCreateTime);
    }

}
