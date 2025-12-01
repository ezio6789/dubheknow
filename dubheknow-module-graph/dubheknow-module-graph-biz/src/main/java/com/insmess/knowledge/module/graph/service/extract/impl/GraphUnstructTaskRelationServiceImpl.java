package com.insmess.knowledge.module.graph.service.extract.impl;

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
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskRelationConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskRelationPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphUnstructTaskRelationMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskRelationService;
/**
 * 任务关系关联Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphUnstructTaskRelationServiceImpl  extends ServiceImpl<GraphUnstructTaskRelationMapper,GraphUnstructTaskRelationPO> implements GraphUnstructTaskRelationService {
    @Resource
    private GraphUnstructTaskRelationMapper graphUnstructTaskRelationMapper;

    @Override
    public Page<GraphUnstructTaskRelationPO> pageGraphUnstructTaskRelation(GraphUnstructTaskRelationPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphUnstructTaskRelationPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphUnstructTaskRelationPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphUnstructTaskRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphUnstructTaskRelation(GraphUnstructTaskRelationSaveReqVO saveReqVO) {
        GraphUnstructTaskRelationPO graphUnstructTaskRelationPO = GraphUnstructTaskRelationConvert.INSTANCE.convertToPO(saveReqVO);
        graphUnstructTaskRelationMapper.insert(graphUnstructTaskRelationPO);
        return graphUnstructTaskRelationPO.getId();
    }

    @Override
    public int updateGraphUnstructTaskRelation(GraphUnstructTaskRelationSaveReqVO updateReqVO) {
        GraphUnstructTaskRelationPO updateObj = GraphUnstructTaskRelationConvert.INSTANCE.convertToPO(updateReqVO);
        return graphUnstructTaskRelationMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphUnstructTaskRelation(Collection<Long> idList) {
        return graphUnstructTaskRelationMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphUnstructTaskRelationPO getGraphUnstructTaskRelationById(Long id) {
        return graphUnstructTaskRelationMapper.selectById(id);
    }

    @Override
    public List<GraphUnstructTaskRelationPO> listGraphUnstructTaskRelation() {
        return graphUnstructTaskRelationMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphUnstructTaskRelationPO> mapGraphUnstructTaskRelation() {
        List<GraphUnstructTaskRelationPO> graphUnstructTaskRelationList = graphUnstructTaskRelationMapper.selectList(null);
        return graphUnstructTaskRelationList.stream()
                .collect(Collectors.toMap(
                        GraphUnstructTaskRelationPO::getId,
                        graphUnstructTaskRelationPO -> graphUnstructTaskRelationPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入任务关系关联数据
         *
         * @param importExcelList 任务关系关联数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphUnstructTaskRelation(List<GraphUnstructTaskRelationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphUnstructTaskRelationRespVO respVO : importExcelList) {
                try {
                    GraphUnstructTaskRelationPO graphUnstructTaskRelationPO = BeanUtils.toBean(respVO, GraphUnstructTaskRelationPO.class);
                    Long graphUnstructTaskRelationId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphUnstructTaskRelationId != null) {
                            GraphUnstructTaskRelationPO existingGraphUnstructTaskRelation = graphUnstructTaskRelationMapper.selectById(graphUnstructTaskRelationId);
                            if (existingGraphUnstructTaskRelation != null) {
                                graphUnstructTaskRelationMapper.updateById(graphUnstructTaskRelationPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphUnstructTaskRelationId + " 的任务关系关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphUnstructTaskRelationId + " 的任务关系关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphUnstructTaskRelationPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphUnstructTaskRelationId);
                        GraphUnstructTaskRelationPO existingGraphUnstructTaskRelation = graphUnstructTaskRelationMapper.selectOne(queryWrapper);
                        if (existingGraphUnstructTaskRelation == null) {
                            graphUnstructTaskRelationMapper.insert(graphUnstructTaskRelationPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphUnstructTaskRelationId + " 的任务关系关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphUnstructTaskRelationId + " 的任务关系关联记录已存在。");
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

    private LambdaQueryWrapperX<GraphUnstructTaskRelationPO> queryCondition(GraphUnstructTaskRelationPageReqVO pageReqVO) {
        GraphUnstructTaskRelationPO graphUnstructTaskRelationPO = GraphUnstructTaskRelationConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphUnstructTaskRelationPO>()
                .eqIfPresent(GraphUnstructTaskRelationPO::getTaskId, graphUnstructTaskRelationPO.getTaskId())
                .eqIfPresent(GraphUnstructTaskRelationPO::getRelationId, graphUnstructTaskRelationPO.getRelationId())
                .eqIfPresent(GraphUnstructTaskRelationPO::getCreateTime, graphUnstructTaskRelationPO.getCreateTime())
                .orderByAsc(GraphUnstructTaskRelationPO::getCreateTime);
    }

}
