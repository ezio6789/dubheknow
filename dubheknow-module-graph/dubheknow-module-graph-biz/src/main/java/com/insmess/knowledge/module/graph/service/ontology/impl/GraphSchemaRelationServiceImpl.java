package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaRelationConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaRelationSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaRelationPO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphSchemaRelationMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaRelationService;
/**
 * 关系配置Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphSchemaRelationServiceImpl  extends ServiceImpl<GraphSchemaRelationMapper,GraphSchemaRelationPO> implements GraphSchemaRelationService {
    @Resource
    private GraphSchemaRelationMapper graphSchemaRelationMapper;

    @Override
    public Page<GraphSchemaRelationPO> pageGraphSchemaRelation(GraphSchemaRelationPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphSchemaRelationPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphSchemaRelationPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        //如果startEndId为空，则添加该条件
        if (pageReqVO.getStartEndId() != null) {
            wrapper.eq(GraphSchemaRelationPO::getStartSchemaId, pageReqVO.getStartEndId())
                    .or()
                    .eq(GraphSchemaRelationPO::getEndSchemaId, pageReqVO.getStartEndId());
        }
        return graphSchemaRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphSchemaRelation(GraphSchemaRelationSaveReqVO saveReqVO) {
        GraphSchemaRelationPO graphSchemaRelationPO = GraphSchemaRelationConvert.INSTANCE.convertToPO(saveReqVO);
        graphSchemaRelationMapper.insert(graphSchemaRelationPO);
        return graphSchemaRelationPO.getId();
    }

    @Override
    public int updateGraphSchemaRelation(GraphSchemaRelationSaveReqVO updateReqVO) {
        GraphSchemaRelationPO updateObj = GraphSchemaRelationConvert.INSTANCE.convertToPO(updateReqVO);
        return graphSchemaRelationMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphSchemaRelation(Collection<Long> idList) {
        return graphSchemaRelationMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphSchemaRelationPO getGraphSchemaRelationById(Long id) {
        return graphSchemaRelationMapper.selectById(id);
    }

    @Override
    public List<GraphSchemaRelationPO> listGraphSchemaRelation() {
        return graphSchemaRelationMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphSchemaRelationPO> mapGraphSchemaRelation() {
        List<GraphSchemaRelationPO> graphSchemaRelationList = graphSchemaRelationMapper.selectList(null);
        return graphSchemaRelationList.stream()
                .collect(Collectors.toMap(
                        GraphSchemaRelationPO::getId,
                        graphSchemaRelationPO -> graphSchemaRelationPO,
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
        public String importGraphSchemaRelation(List<GraphSchemaRelationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphSchemaRelationRespVO respVO : importExcelList) {
                try {
                    GraphSchemaRelationPO graphSchemaRelationPO = BeanUtils.toBean(respVO, GraphSchemaRelationPO.class);
                    Long graphSchemaRelationId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphSchemaRelationId != null) {
                            GraphSchemaRelationPO existingGraphSchemaRelation = graphSchemaRelationMapper.selectById(graphSchemaRelationId);
                            if (existingGraphSchemaRelation != null) {
                                graphSchemaRelationMapper.updateById(graphSchemaRelationPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphSchemaRelationId + " 的关系配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphSchemaRelationId + " 的关系配置记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphSchemaRelationPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphSchemaRelationId);
                        GraphSchemaRelationPO existingGraphSchemaRelation = graphSchemaRelationMapper.selectOne(queryWrapper);
                        if (existingGraphSchemaRelation == null) {
                            graphSchemaRelationMapper.insert(graphSchemaRelationPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphSchemaRelationId + " 的关系配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphSchemaRelationId + " 的关系配置记录已存在。");
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

    private LambdaQueryWrapperX<GraphSchemaRelationPO> queryCondition(GraphSchemaRelationPageReqVO pageReqVO) {
        GraphSchemaRelationPO graphSchemaRelationPO = GraphSchemaRelationConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphSchemaRelationPO>()
                .eqIfPresent(GraphSchemaRelationPO::getStartSchemaId, graphSchemaRelationPO.getStartSchemaId())
                .eqIfPresent(GraphSchemaRelationPO::getRelation, graphSchemaRelationPO.getRelation())
                .eqIfPresent(GraphSchemaRelationPO::getEndSchemaId, graphSchemaRelationPO.getEndSchemaId())
                .eqIfPresent(GraphSchemaRelationPO::getInverseFlag, graphSchemaRelationPO.getInverseFlag())
                .eqIfPresent(GraphSchemaRelationPO::getCreateTime, graphSchemaRelationPO.getCreateTime())
                .eqIfPresent(GraphSchemaRelationPO::getKnowledgeId, graphSchemaRelationPO.getKnowledgeId())
                .orderByAsc(GraphSchemaRelationPO::getCreateTime);
    }

}
