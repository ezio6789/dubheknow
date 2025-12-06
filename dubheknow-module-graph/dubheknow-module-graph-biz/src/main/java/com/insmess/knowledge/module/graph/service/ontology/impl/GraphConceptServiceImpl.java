package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptPO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptRelationPO;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptRelationService;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptSaveReqVO;
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
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRespVO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphConceptMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptService;
/**
 * 概念配置Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphConceptServiceImpl extends ServiceImpl<GraphConceptMapper, GraphConceptPO> implements GraphConceptService {
    @Resource
    private GraphConceptMapper graphConceptMapper;

    @Resource
    private GraphConceptRelationService graphConceptRelationService;


    @Override
    public List<GraphConceptPO> listExtConceptTree(GraphConceptPageReqVO extConcept) {
        return getExtConceptListByPid(0L, extConcept.getKnowledgeId());
    }

    private List<GraphConceptPO> getExtConceptListByPid(Long pid, Long knowledgeId) {
        //根据pid获取
        List<GraphConceptPO> extConceptDOList = graphConceptMapper.selectList(
                new QueryWrapper<GraphConceptPO>().lambda()
                        .eq(GraphConceptPO::getPid, pid)
                        .eq(GraphConceptPO::getKnowledgeId, knowledgeId)
        );
        //extConceptDOList中的每个子内容
        for (GraphConceptPO extConceptDO : extConceptDOList) {
            extConceptDO.setChildren(getExtConceptListByPid(extConceptDO.getId(), knowledgeId));
        }
        return extConceptDOList;
    }

    @Override
    public List<GraphConceptPO> getParentListByPid(Long id) {
        GraphConceptPO extConceptDO = graphConceptMapper.selectById(id);
        //如果是空
        if (extConceptDO == null) {
            return new ArrayList<>();
        }
        List<GraphConceptPO> parentList = new ArrayList<>();
        parentList.add(extConceptDO);
        //如果不是根节点
        if (!extConceptDO.getPid().equals(0L)) {
            List<GraphConceptPO> resultList = getParentListByPid(extConceptDO.getPid());
            parentList.addAll(resultList);
        }
        return parentList;
    }

    @Override
    public Map<String, Object> getGraphOntology(Long knowledgeId) {
        //定义map集合，保存数据
        Map<String, Object> map = new HashMap<>();
        //查询该知识图谱下所有的本体（列表）
        List<GraphConceptPO> extConceptList = list(
                new QueryWrapper<GraphConceptPO>().lambda().eq(GraphConceptPO::getKnowledgeId, knowledgeId)
        );
        map.put("entities", extConceptList);
        //查询该知识图谱下所有的关系（列表）
        List<GraphConceptRelationPO> extConceptRelationList = graphConceptRelationService.list(
                new QueryWrapper<GraphConceptRelationPO>().lambda().eq(GraphConceptRelationPO::getKnowledgeId, knowledgeId)
        );
        //封装关系
        List<Map<String, Object>> extConceptRelationListMap = new ArrayList<>();
        for (GraphConceptRelationPO conceptRelationPO : extConceptRelationList) {
            Map<String, Object> map2 = new HashMap<>();
            //查询起点和终点实体名称
            map2.put("startId", conceptRelationPO.getStartConceptId());
            map2.put("endId", conceptRelationPO.getEndConceptId());
            GraphConceptPO startConcept = graphConceptMapper.selectById(conceptRelationPO.getStartConceptId());
            GraphConceptPO endConcept = graphConceptMapper.selectById(conceptRelationPO.getEndConceptId());
            map2.put("startName", startConcept.getName());
            map2.put("endName", endConcept.getName());
            map2.put("id", conceptRelationPO.getId());
            map2.put("relationType", conceptRelationPO.getRelation());
            extConceptRelationListMap.add(map2);
        }
        map.put("relationships", extConceptRelationListMap);
        return map;
    }

    @Override
    public Page<GraphConceptPO> pageGraphConcept(GraphConceptPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphConceptPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphConceptPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphConceptMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphConcept(GraphConceptSaveReqVO saveReqVO) {
        GraphConceptPO graphConceptPO = GraphConceptConvert.INSTANCE.convertToPO(saveReqVO);
        graphConceptMapper.insert(graphConceptPO);
        return graphConceptPO.getId();
    }

    @Override
    public int updateGraphConcept(GraphConceptSaveReqVO updateReqVO) {
        GraphConceptPO updateObj = GraphConceptConvert.INSTANCE.convertToPO(updateReqVO);
        return graphConceptMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphConcept(Collection<Long> idList) {
        return graphConceptMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphConceptPO getGraphConceptById(Long id) {
        return graphConceptMapper.selectById(id);
    }

    @Override
    public List<GraphConceptPO> listGraphConcept() {
        return graphConceptMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphConceptPO> mapGraphConcept() {
        List<GraphConceptPO> graphConceptList = graphConceptMapper.selectList(null);
        return graphConceptList.stream()
                .collect(Collectors.toMap(
                        GraphConceptPO::getId,
                        graphConceptPO -> graphConceptPO,
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
        public String importGraphConcept(List<GraphConceptRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphConceptRespVO respVO : importExcelList) {
                try {
                    GraphConceptPO graphConceptPO = BeanUtils.toBean(respVO, GraphConceptPO.class);
                    Long graphConceptId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphConceptId != null) {
                            GraphConceptPO existingGraphConcept = graphConceptMapper.selectById(graphConceptId);
                            if (existingGraphConcept != null) {
                                graphConceptMapper.updateById(graphConceptPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphConceptId + " 的概念配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphConceptId + " 的概念配置记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphConceptPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphConceptId);
                        GraphConceptPO existingGraphConcept = graphConceptMapper.selectOne(queryWrapper);
                        if (existingGraphConcept == null) {
                            graphConceptMapper.insert(graphConceptPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphConceptId + " 的概念配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphConceptId + " 的概念配置记录已存在。");
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

    private LambdaQueryWrapperX<GraphConceptPO> queryCondition(GraphConceptPageReqVO pageReqVO) {
        GraphConceptPO graphConceptPO = GraphConceptConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphConceptPO>()
                .likeIfPresent(GraphConceptPO::getName, graphConceptPO.getName())
                .eqIfPresent(GraphConceptPO::getDescription, graphConceptPO.getDescription())
                .eqIfPresent(GraphConceptPO::getColor, graphConceptPO.getColor())
                .eqIfPresent(GraphConceptPO::getCreateTime, graphConceptPO.getCreateTime())
                .eqIfPresent(GraphConceptPO::getKnowledgeId, graphConceptPO.getKnowledgeId())
                .eqIfPresent(GraphConceptPO::getPid, graphConceptPO.getPid())
                .orderByAsc(GraphConceptPO::getCreateTime);
    }

}
