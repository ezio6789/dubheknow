package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptRelationConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptRelationPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationPageReqVO;
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
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptRelationSaveReqVO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphConceptRelationMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptRelationService;
/**
 * 关系配置Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphConceptRelationServiceImpl extends ServiceImpl<GraphConceptRelationMapper, GraphConceptRelationPO> implements GraphConceptRelationService {
    @Resource
    private GraphConceptRelationMapper graphConceptRelationMapper;

    @Override
    public Page<GraphConceptRelationPO> pageGraphConceptRelation(GraphConceptRelationPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphConceptRelationPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphConceptRelationPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        //如果startEndId为空，则添加该条件
        if (pageReqVO.getStartEndId() != null) {
            wrapper.eq(GraphConceptRelationPO::getStartConceptId, pageReqVO.getStartEndId())
                    .or()
                    .eq(GraphConceptRelationPO::getEndConceptId, pageReqVO.getStartEndId());
        }
        return graphConceptRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphConceptRelation(GraphConceptRelationSaveReqVO saveReqVO) {
        GraphConceptRelationPO graphConceptRelationPO = GraphConceptRelationConvert.INSTANCE.convertToPO(saveReqVO);
        graphConceptRelationMapper.insert(graphConceptRelationPO);
        return graphConceptRelationPO.getId();
    }

    @Override
    public int updateGraphConceptRelation(GraphConceptRelationSaveReqVO updateReqVO) {
        GraphConceptRelationPO updateObj = GraphConceptRelationConvert.INSTANCE.convertToPO(updateReqVO);
        return graphConceptRelationMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphConceptRelation(Collection<Long> idList) {
        return graphConceptRelationMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphConceptRelationPO getGraphConceptRelationById(Long id) {
        return graphConceptRelationMapper.selectById(id);
    }

    @Override
    public List<GraphConceptRelationPO> listGraphConceptRelation() {
        return graphConceptRelationMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphConceptRelationPO> mapGraphConceptRelation() {
        List<GraphConceptRelationPO> graphConceptRelationList = graphConceptRelationMapper.selectList(null);
        return graphConceptRelationList.stream()
                .collect(Collectors.toMap(
                        GraphConceptRelationPO::getId,
                        graphConceptRelationPO -> graphConceptRelationPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入关系配置数据
         *
         * @param importExcelList 关系配置数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphConceptRelation(List<GraphConceptRelationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphConceptRelationRespVO respVO : importExcelList) {
                try {
                    GraphConceptRelationPO graphConceptRelationPO = BeanUtils.toBean(respVO, GraphConceptRelationPO.class);
                    Long graphConceptRelationId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphConceptRelationId != null) {
                            GraphConceptRelationPO existingGraphConceptRelation = graphConceptRelationMapper.selectById(graphConceptRelationId);
                            if (existingGraphConceptRelation != null) {
                                graphConceptRelationMapper.updateById(graphConceptRelationPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphConceptRelationId + " 的关系配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphConceptRelationId + " 的关系配置记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphConceptRelationPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphConceptRelationId);
                        GraphConceptRelationPO existingGraphConceptRelation = graphConceptRelationMapper.selectOne(queryWrapper);
                        if (existingGraphConceptRelation == null) {
                            graphConceptRelationMapper.insert(graphConceptRelationPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphConceptRelationId + " 的关系配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphConceptRelationId + " 的关系配置记录已存在。");
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

    private LambdaQueryWrapperX<GraphConceptRelationPO> queryCondition(GraphConceptRelationPageReqVO pageReqVO) {
        GraphConceptRelationPO graphConceptRelationPO = GraphConceptRelationConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphConceptRelationPO>()
                .eqIfPresent(GraphConceptRelationPO::getStartConceptId, graphConceptRelationPO.getStartConceptId())
                .eqIfPresent(GraphConceptRelationPO::getRelation, graphConceptRelationPO.getRelation())
                .eqIfPresent(GraphConceptRelationPO::getEndConceptId, graphConceptRelationPO.getEndConceptId())
                .eqIfPresent(GraphConceptRelationPO::getInverseFlag, graphConceptRelationPO.getInverseFlag())
                .eqIfPresent(GraphConceptRelationPO::getCreateTime, graphConceptRelationPO.getCreateTime())
                .eqIfPresent(GraphConceptRelationPO::getKnowledgeId, graphConceptRelationPO.getKnowledgeId())
                .orderByAsc(GraphConceptRelationPO::getCreateTime);
    }

}
