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
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaMappingConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaMappingPO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphSchemaMappingMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaMappingService;
/**
 * 概念映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphSchemaMappingServiceImpl  extends ServiceImpl<GraphSchemaMappingMapper,GraphSchemaMappingPO> implements GraphSchemaMappingService {
    @Resource
    private GraphSchemaMappingMapper graphSchemaMappingMapper;

    @Override
    public List<GraphSchemaMappingPO> listByTaskId(Long taskId) {
        return list(new QueryWrapper<GraphSchemaMappingPO>().lambda().eq(GraphSchemaMappingPO::getTaskId, taskId));
    }

    @Override
    public Page<GraphSchemaMappingPO> pageGraphSchemaMapping(GraphSchemaMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphSchemaMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphSchemaMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphSchemaMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphSchemaMapping(GraphSchemaMappingSaveReqVO saveReqVO) {
        GraphSchemaMappingPO graphSchemaMappingPO = GraphSchemaMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphSchemaMappingMapper.insert(graphSchemaMappingPO);
        return graphSchemaMappingPO.getId();
    }

    @Override
    public int updateGraphSchemaMapping(GraphSchemaMappingSaveReqVO updateReqVO) {
        GraphSchemaMappingPO updateObj = GraphSchemaMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphSchemaMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphSchemaMapping(Collection<Long> idList) {
        return graphSchemaMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphSchemaMappingPO getGraphSchemaMappingById(Long id) {
        return graphSchemaMappingMapper.selectById(id);
    }

    @Override
    public List<GraphSchemaMappingPO> listGraphSchemaMapping() {
        return graphSchemaMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphSchemaMappingPO> mapGraphSchemaMapping() {
        List<GraphSchemaMappingPO> graphSchemaMappingList = graphSchemaMappingMapper.selectList(null);
        return graphSchemaMappingList.stream()
                .collect(Collectors.toMap(
                        GraphSchemaMappingPO::getId,
                        graphSchemaMappingPO -> graphSchemaMappingPO,
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
        public String importGraphSchemaMapping(List<GraphSchemaMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphSchemaMappingRespVO respVO : importExcelList) {
                try {
                    GraphSchemaMappingPO graphSchemaMappingPO = BeanUtils.toBean(respVO, GraphSchemaMappingPO.class);
                    Long graphSchemaMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphSchemaMappingId != null) {
                            GraphSchemaMappingPO existingGraphSchemaMapping = graphSchemaMappingMapper.selectById(graphSchemaMappingId);
                            if (existingGraphSchemaMapping != null) {
                                graphSchemaMappingMapper.updateById(graphSchemaMappingPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphSchemaMappingId + " 的概念映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphSchemaMappingId + " 的概念映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphSchemaMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphSchemaMappingId);
                        GraphSchemaMappingPO existingGraphSchemaMapping = graphSchemaMappingMapper.selectOne(queryWrapper);
                        if (existingGraphSchemaMapping == null) {
                            graphSchemaMappingMapper.insert(graphSchemaMappingPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphSchemaMappingId + " 的概念映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphSchemaMappingId + " 的概念映射记录已存在。");
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

    private LambdaQueryWrapperX<GraphSchemaMappingPO> queryCondition(GraphSchemaMappingPageReqVO pageReqVO) {
        GraphSchemaMappingPO graphSchemaMappingPO = GraphSchemaMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphSchemaMappingPO>()
                .eqIfPresent(GraphSchemaMappingPO::getTaskId, graphSchemaMappingPO.getTaskId())
                .likeIfPresent(GraphSchemaMappingPO::getTableName, graphSchemaMappingPO.getTableName())
                .eqIfPresent(GraphSchemaMappingPO::getTableComment, graphSchemaMappingPO.getTableComment())
                .eqIfPresent(GraphSchemaMappingPO::getEntityNameField, graphSchemaMappingPO.getEntityNameField())
                .eqIfPresent(GraphSchemaMappingPO::getSchemaId, graphSchemaMappingPO.getSchemaId())
                .likeIfPresent(GraphSchemaMappingPO::getSchemaName, graphSchemaMappingPO.getSchemaName())
                .eqIfPresent(GraphSchemaMappingPO::getCreateTime, graphSchemaMappingPO.getCreateTime())
                .orderByAsc(GraphSchemaMappingPO::getCreateTime);
    }

}
