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
import com.insmess.knowledge.module.graph.convert.ontology.GraphRelationMappingConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphRelationMappingPO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphRelationMappingMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphRelationMappingService;
/**
 * 关系映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphRelationMappingServiceImpl  extends ServiceImpl<GraphRelationMappingMapper,GraphRelationMappingPO> implements GraphRelationMappingService {
    @Resource
    private GraphRelationMappingMapper graphRelationMappingMapper;

    @Override
    public Page<GraphRelationMappingPO> pageGraphRelationMapping(GraphRelationMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphRelationMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphRelationMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphRelationMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphRelationMapping(GraphRelationMappingSaveReqVO saveReqVO) {
        GraphRelationMappingPO graphRelationMappingPO = GraphRelationMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphRelationMappingMapper.insert(graphRelationMappingPO);
        return graphRelationMappingPO.getId();
    }

    @Override
    public int updateGraphRelationMapping(GraphRelationMappingSaveReqVO updateReqVO) {
        GraphRelationMappingPO updateObj = GraphRelationMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphRelationMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphRelationMapping(Collection<Long> idList) {
        return graphRelationMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphRelationMappingPO getGraphRelationMappingById(Long id) {
        return graphRelationMappingMapper.selectById(id);
    }

    @Override
    public List<GraphRelationMappingPO> listGraphRelationMapping() {
        return graphRelationMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphRelationMappingPO> mapGraphRelationMapping() {
        List<GraphRelationMappingPO> graphRelationMappingList = graphRelationMappingMapper.selectList(null);
        return graphRelationMappingList.stream()
                .collect(Collectors.toMap(
                        GraphRelationMappingPO::getId,
                        graphRelationMappingPO -> graphRelationMappingPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入关系映射数据
         *
         * @param importExcelList 关系映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphRelationMapping(List<GraphRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphRelationMappingRespVO respVO : importExcelList) {
                try {
                    GraphRelationMappingPO graphRelationMappingPO = BeanUtils.toBean(respVO, GraphRelationMappingPO.class);
                    Long graphRelationMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphRelationMappingId != null) {
                            GraphRelationMappingPO existingGraphRelationMapping = graphRelationMappingMapper.selectById(graphRelationMappingId);
                            if (existingGraphRelationMapping != null) {
                                graphRelationMappingMapper.updateById(graphRelationMappingPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphRelationMappingId + " 的关系映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphRelationMappingId + " 的关系映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphRelationMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphRelationMappingId);
                        GraphRelationMappingPO existingGraphRelationMapping = graphRelationMappingMapper.selectOne(queryWrapper);
                        if (existingGraphRelationMapping == null) {
                            graphRelationMappingMapper.insert(graphRelationMappingPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphRelationMappingId + " 的关系映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphRelationMappingId + " 的关系映射记录已存在。");
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

    @Override
    public List<GraphRelationMappingPO> list(GraphRelationMappingPageReqVO relationMappingPageReqVO) {
        return list(queryCondition(relationMappingPageReqVO));
    }

    private LambdaQueryWrapperX<GraphRelationMappingPO> queryCondition(GraphRelationMappingPageReqVO pageReqVO) {
        GraphRelationMappingPO graphRelationMappingPO = GraphRelationMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphRelationMappingPO>()
                .eqIfPresent(GraphRelationMappingPO::getTaskId, graphRelationMappingPO.getTaskId())
                .likeIfPresent(GraphRelationMappingPO::getTableName, graphRelationMappingPO.getTableName())
                .eqIfPresent(GraphRelationMappingPO::getTableComment, graphRelationMappingPO.getTableComment())
                .likeIfPresent(GraphRelationMappingPO::getFieldName, graphRelationMappingPO.getFieldName())
                .eqIfPresent(GraphRelationMappingPO::getFieldComment, graphRelationMappingPO.getFieldComment())
                .eqIfPresent(GraphRelationMappingPO::getRelation, graphRelationMappingPO.getRelation())
                .eqIfPresent(GraphRelationMappingPO::getRelationTable, graphRelationMappingPO.getRelationTable())
                .likeIfPresent(GraphRelationMappingPO::getRelationTableName, graphRelationMappingPO.getRelationTableName())
                .eqIfPresent(GraphRelationMappingPO::getRelationField, graphRelationMappingPO.getRelationField())
                .eqIfPresent(GraphRelationMappingPO::getRelationNameField, graphRelationMappingPO.getRelationNameField())
                .eqIfPresent(GraphRelationMappingPO::getCreateTime, graphRelationMappingPO.getCreateTime())
                .orderByAsc(GraphRelationMappingPO::getCreateTime);
    }

}
