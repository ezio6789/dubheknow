package com.insmess.knowledge.module.graph.service.extract.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskRelationMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskRelationMappingConvert;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphStructTaskRelationMappingMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskRelationMappingService;
/**
 * 关系映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphStructTaskRelationMappingServiceImpl extends ServiceImpl<GraphStructTaskRelationMappingMapper, GraphStructTaskRelationMappingPO> implements GraphStructTaskRelationMappingService {
    @Resource
    private GraphStructTaskRelationMappingMapper graphStructTaskRelationMappingMapper;

    @Override
    public Page<GraphStructTaskRelationMappingPO> pageGraphRelationMapping(GraphStructTaskRelationMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphStructTaskRelationMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphStructTaskRelationMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphStructTaskRelationMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphRelationMapping(GraphStructTaskRelationMappingSaveReqVO saveReqVO) {
        GraphStructTaskRelationMappingPO graphStructTaskRelationMappingPO = GraphStructTaskRelationMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphStructTaskRelationMappingMapper.insert(graphStructTaskRelationMappingPO);
        return graphStructTaskRelationMappingPO.getId();
    }

    @Override
    public int updateGraphRelationMapping(GraphStructTaskRelationMappingSaveReqVO updateReqVO) {
        GraphStructTaskRelationMappingPO updateObj = GraphStructTaskRelationMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphStructTaskRelationMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphRelationMapping(Collection<Long> idList) {
        return graphStructTaskRelationMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphStructTaskRelationMappingPO getGraphRelationMappingById(Long id) {
        return graphStructTaskRelationMappingMapper.selectById(id);
    }

    @Override
    public List<GraphStructTaskRelationMappingPO> listGraphRelationMapping() {
        return graphStructTaskRelationMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphStructTaskRelationMappingPO> mapGraphRelationMapping() {
        List<GraphStructTaskRelationMappingPO> graphRelationMappingList = graphStructTaskRelationMappingMapper.selectList(null);
        return graphRelationMappingList.stream()
                .collect(Collectors.toMap(
                        GraphStructTaskRelationMappingPO::getId,
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
        public String importGraphRelationMapping(List<GraphStructTaskRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphStructTaskRelationMappingRespVO respVO : importExcelList) {
                try {
                    GraphStructTaskRelationMappingPO graphStructTaskRelationMappingPO = BeanUtils.toBean(respVO, GraphStructTaskRelationMappingPO.class);
                    Long graphRelationMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphRelationMappingId != null) {
                            GraphStructTaskRelationMappingPO existingGraphRelationMapping = graphStructTaskRelationMappingMapper.selectById(graphRelationMappingId);
                            if (existingGraphRelationMapping != null) {
                                graphStructTaskRelationMappingMapper.updateById(graphStructTaskRelationMappingPO);
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
                        QueryWrapper<GraphStructTaskRelationMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphRelationMappingId);
                        GraphStructTaskRelationMappingPO existingGraphRelationMapping = graphStructTaskRelationMappingMapper.selectOne(queryWrapper);
                        if (existingGraphRelationMapping == null) {
                            graphStructTaskRelationMappingMapper.insert(graphStructTaskRelationMappingPO);
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
    public List<GraphStructTaskRelationMappingPO> list(GraphStructTaskRelationMappingPageReqVO relationMappingPageReqVO) {
        return list(queryCondition(relationMappingPageReqVO));
    }

    @Override
    public List<GraphStructTaskRelationMappingPO> listByTaskId(Long id) {
        GraphStructTaskRelationMappingPageReqVO params = new GraphStructTaskRelationMappingPageReqVO();
        params.setTaskId(id);
        return list(params);
    }

    private LambdaQueryWrapperX<GraphStructTaskRelationMappingPO> queryCondition(GraphStructTaskRelationMappingPageReqVO pageReqVO) {
        GraphStructTaskRelationMappingPO graphStructTaskRelationMappingPO = GraphStructTaskRelationMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphStructTaskRelationMappingPO>()
                .eqIfPresent(GraphStructTaskRelationMappingPO::getTaskId, graphStructTaskRelationMappingPO.getTaskId())
                .likeIfPresent(GraphStructTaskRelationMappingPO::getTableName, graphStructTaskRelationMappingPO.getTableName())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getTableComment, graphStructTaskRelationMappingPO.getTableComment())
                .likeIfPresent(GraphStructTaskRelationMappingPO::getFieldName, graphStructTaskRelationMappingPO.getFieldName())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getFieldComment, graphStructTaskRelationMappingPO.getFieldComment())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getRelation, graphStructTaskRelationMappingPO.getRelation())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getRelationTable, graphStructTaskRelationMappingPO.getRelationTable())
                .likeIfPresent(GraphStructTaskRelationMappingPO::getRelationTableName, graphStructTaskRelationMappingPO.getRelationTableName())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getRelationField, graphStructTaskRelationMappingPO.getRelationField())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getRelationNameField, graphStructTaskRelationMappingPO.getRelationNameField())
                .eqIfPresent(GraphStructTaskRelationMappingPO::getCreateTime, graphStructTaskRelationMappingPO.getCreateTime())
                .orderByAsc(GraphStructTaskRelationMappingPO::getCreateTime);
    }

}
