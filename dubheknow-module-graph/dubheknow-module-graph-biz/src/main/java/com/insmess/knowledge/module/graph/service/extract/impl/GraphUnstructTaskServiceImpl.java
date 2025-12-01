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
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphUnstructTaskMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskService;
/**
 * 非结构化抽取任务Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphUnstructTaskServiceImpl  extends ServiceImpl<GraphUnstructTaskMapper,GraphUnstructTaskPO> implements GraphUnstructTaskService {
    @Resource
    private GraphUnstructTaskMapper graphUnstructTaskMapper;

    @Override
    public Page<GraphUnstructTaskPO> pageGraphUnstructTask(GraphUnstructTaskPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphUnstructTaskPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphUnstructTaskPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphUnstructTaskMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphUnstructTask(GraphUnstructTaskSaveReqVO saveReqVO) {
        GraphUnstructTaskPO graphUnstructTaskPO = GraphUnstructTaskConvert.INSTANCE.convertToPO(saveReqVO);
        graphUnstructTaskMapper.insert(graphUnstructTaskPO);
        return graphUnstructTaskPO.getId();
    }

    @Override
    public int updateGraphUnstructTask(GraphUnstructTaskSaveReqVO updateReqVO) {
        GraphUnstructTaskPO updateObj = GraphUnstructTaskConvert.INSTANCE.convertToPO(updateReqVO);
        return graphUnstructTaskMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphUnstructTask(Collection<Long> idList) {
        return graphUnstructTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphUnstructTaskPO getGraphUnstructTaskById(Long id) {
        return graphUnstructTaskMapper.selectById(id);
    }

    @Override
    public List<GraphUnstructTaskPO> listGraphUnstructTask() {
        return graphUnstructTaskMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphUnstructTaskPO> mapGraphUnstructTask() {
        List<GraphUnstructTaskPO> graphUnstructTaskList = graphUnstructTaskMapper.selectList(null);
        return graphUnstructTaskList.stream()
                .collect(Collectors.toMap(
                        GraphUnstructTaskPO::getId,
                        graphUnstructTaskPO -> graphUnstructTaskPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入非结构化抽取任务数据
         *
         * @param importExcelList 非结构化抽取任务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphUnstructTask(List<GraphUnstructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphUnstructTaskRespVO respVO : importExcelList) {
                try {
                    GraphUnstructTaskPO graphUnstructTaskPO = BeanUtils.toBean(respVO, GraphUnstructTaskPO.class);
                    Long graphUnstructTaskId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphUnstructTaskId != null) {
                            GraphUnstructTaskPO existingGraphUnstructTask = graphUnstructTaskMapper.selectById(graphUnstructTaskId);
                            if (existingGraphUnstructTask != null) {
                                graphUnstructTaskMapper.updateById(graphUnstructTaskPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphUnstructTaskId + " 的非结构化抽取任务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphUnstructTaskId + " 的非结构化抽取任务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphUnstructTaskPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphUnstructTaskId);
                        GraphUnstructTaskPO existingGraphUnstructTask = graphUnstructTaskMapper.selectOne(queryWrapper);
                        if (existingGraphUnstructTask == null) {
                            graphUnstructTaskMapper.insert(graphUnstructTaskPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphUnstructTaskId + " 的非结构化抽取任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphUnstructTaskId + " 的非结构化抽取任务记录已存在。");
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

    private LambdaQueryWrapperX<GraphUnstructTaskPO> queryCondition(GraphUnstructTaskPageReqVO pageReqVO) {
        GraphUnstructTaskPO graphUnstructTaskPO = GraphUnstructTaskConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphUnstructTaskPO>()
                .eqIfPresent(GraphUnstructTaskPO::getKnowledgeId, graphUnstructTaskPO.getKnowledgeId())
                .likeIfPresent(GraphUnstructTaskPO::getName, graphUnstructTaskPO.getName())
                .eqIfPresent(GraphUnstructTaskPO::getStatus, graphUnstructTaskPO.getStatus())
                .eqIfPresent(GraphUnstructTaskPO::getPublishStatus, graphUnstructTaskPO.getPublishStatus())
                .eqIfPresent(GraphUnstructTaskPO::getExtractMode, graphUnstructTaskPO.getExtractMode())
                .eqIfPresent(GraphUnstructTaskPO::getPublishTime, graphUnstructTaskPO.getPublishTime())
                .eqIfPresent(GraphUnstructTaskPO::getPublisherId, graphUnstructTaskPO.getPublisherId())
                .eqIfPresent(GraphUnstructTaskPO::getPublishBy, graphUnstructTaskPO.getPublishBy())
                .eqIfPresent(GraphUnstructTaskPO::getCreateTime, graphUnstructTaskPO.getCreateTime())
                .orderByAsc(GraphUnstructTaskPO::getCreateTime);
    }

}
