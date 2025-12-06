package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptMappingConvert;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphConceptMappingMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptMappingService;
/**
 * 概念映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphConceptMappingServiceImpl extends ServiceImpl<GraphConceptMappingMapper, GraphConceptMappingPO> implements GraphConceptMappingService {
    @Resource
    private GraphConceptMappingMapper graphConceptMappingMapper;

    @Override
    public List<GraphConceptMappingPO> listByTaskId(Long taskId) {
        return list(new QueryWrapper<GraphConceptMappingPO>().lambda().eq(GraphConceptMappingPO::getTaskId, taskId));
    }

    @Override
    public Page<GraphConceptMappingPO> pageGraphConceptMapping(GraphConceptMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphConceptMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphConceptMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphConceptMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphConceptMapping(GraphConceptMappingSaveReqVO saveReqVO) {
        GraphConceptMappingPO graphConceptMappingPO = GraphConceptMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphConceptMappingMapper.insert(graphConceptMappingPO);
        return graphConceptMappingPO.getId();
    }

    @Override
    public int updateGraphConceptMapping(GraphConceptMappingSaveReqVO updateReqVO) {
        GraphConceptMappingPO updateObj = GraphConceptMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphConceptMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphConceptMapping(Collection<Long> idList) {
        return graphConceptMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphConceptMappingPO getGraphConceptMappingById(Long id) {
        return graphConceptMappingMapper.selectById(id);
    }

    @Override
    public List<GraphConceptMappingPO> listGraphConceptMapping() {
        return graphConceptMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphConceptMappingPO> mapGraphConceptMapping() {
        List<GraphConceptMappingPO> graphConceptMappingList = graphConceptMappingMapper.selectList(null);
        return graphConceptMappingList.stream()
                .collect(Collectors.toMap(
                        GraphConceptMappingPO::getId,
                        graphConceptMappingPO -> graphConceptMappingPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入概念映射数据
         *
         * @param importExcelList 概念映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphConceptMapping(List<GraphConceptMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphConceptMappingRespVO respVO : importExcelList) {
                try {
                    GraphConceptMappingPO graphConceptMappingPO = BeanUtils.toBean(respVO, GraphConceptMappingPO.class);
                    Long graphConceptMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphConceptMappingId != null) {
                            GraphConceptMappingPO existingGraphConceptMapping = graphConceptMappingMapper.selectById(graphConceptMappingId);
                            if (existingGraphConceptMapping != null) {
                                graphConceptMappingMapper.updateById(graphConceptMappingPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphConceptMappingId + " 的概念映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphConceptMappingId + " 的概念映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphConceptMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphConceptMappingId);
                        GraphConceptMappingPO existingGraphConceptMapping = graphConceptMappingMapper.selectOne(queryWrapper);
                        if (existingGraphConceptMapping == null) {
                            graphConceptMappingMapper.insert(graphConceptMappingPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphConceptMappingId + " 的概念映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphConceptMappingId + " 的概念映射记录已存在。");
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

    private LambdaQueryWrapperX<GraphConceptMappingPO> queryCondition(GraphConceptMappingPageReqVO pageReqVO) {
        GraphConceptMappingPO graphConceptMappingPO = GraphConceptMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphConceptMappingPO>()
                .eqIfPresent(GraphConceptMappingPO::getTaskId, graphConceptMappingPO.getTaskId())
                .likeIfPresent(GraphConceptMappingPO::getTableName, graphConceptMappingPO.getTableName())
                .eqIfPresent(GraphConceptMappingPO::getTableComment, graphConceptMappingPO.getTableComment())
                .eqIfPresent(GraphConceptMappingPO::getEntityNameField, graphConceptMappingPO.getEntityNameField())
                .eqIfPresent(GraphConceptMappingPO::getConceptId, graphConceptMappingPO.getConceptId())
                .likeIfPresent(GraphConceptMappingPO::getConceptName, graphConceptMappingPO.getConceptName())
                .eqIfPresent(GraphConceptMappingPO::getCreateTime, graphConceptMappingPO.getCreateTime())
                .orderByAsc(GraphConceptMappingPO::getCreateTime);
    }

}
