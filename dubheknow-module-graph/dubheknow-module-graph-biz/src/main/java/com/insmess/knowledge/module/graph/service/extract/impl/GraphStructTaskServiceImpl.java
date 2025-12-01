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
import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphStructTaskMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskService;
/**
 * 结构化抽取任务Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphStructTaskServiceImpl  extends ServiceImpl<GraphStructTaskMapper,GraphStructTaskPO> implements GraphStructTaskService {
    @Resource
    private GraphStructTaskMapper graphStructTaskMapper;

    @Override
    public Page<GraphStructTaskPO> pageGraphStructTask(GraphStructTaskPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphStructTaskPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphStructTaskPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphStructTaskMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphStructTask(GraphStructTaskSaveReqVO saveReqVO) {
        GraphStructTaskPO graphStructTaskPO = GraphStructTaskConvert.INSTANCE.convertToPO(saveReqVO);
        graphStructTaskMapper.insert(graphStructTaskPO);
        return graphStructTaskPO.getId();
    }

    @Override
    public int updateGraphStructTask(GraphStructTaskSaveReqVO updateReqVO) {
        GraphStructTaskPO updateObj = GraphStructTaskConvert.INSTANCE.convertToPO(updateReqVO);
        return graphStructTaskMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphStructTask(Collection<Long> idList) {
        return graphStructTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphStructTaskPO getGraphStructTaskById(Long id) {
        return graphStructTaskMapper.selectById(id);
    }

    @Override
    public List<GraphStructTaskPO> listGraphStructTask() {
        return graphStructTaskMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphStructTaskPO> mapGraphStructTask() {
        List<GraphStructTaskPO> graphStructTaskList = graphStructTaskMapper.selectList(null);
        return graphStructTaskList.stream()
                .collect(Collectors.toMap(
                        GraphStructTaskPO::getId,
                        graphStructTaskPO -> graphStructTaskPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入结构化抽取任务数据
         *
         * @param importExcelList 结构化抽取任务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphStructTask(List<GraphStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphStructTaskRespVO respVO : importExcelList) {
                try {
                    GraphStructTaskPO graphStructTaskPO = BeanUtils.toBean(respVO, GraphStructTaskPO.class);
                    Long graphStructTaskId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphStructTaskId != null) {
                            GraphStructTaskPO existingGraphStructTask = graphStructTaskMapper.selectById(graphStructTaskId);
                            if (existingGraphStructTask != null) {
                                graphStructTaskMapper.updateById(graphStructTaskPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphStructTaskId + " 的结构化抽取任务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphStructTaskId + " 的结构化抽取任务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphStructTaskPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphStructTaskId);
                        GraphStructTaskPO existingGraphStructTask = graphStructTaskMapper.selectOne(queryWrapper);
                        if (existingGraphStructTask == null) {
                            graphStructTaskMapper.insert(graphStructTaskPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphStructTaskId + " 的结构化抽取任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphStructTaskId + " 的结构化抽取任务记录已存在。");
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

    private LambdaQueryWrapperX<GraphStructTaskPO> queryCondition(GraphStructTaskPageReqVO pageReqVO) {
        GraphStructTaskPO graphStructTaskPO = GraphStructTaskConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphStructTaskPO>()
                .eqIfPresent(GraphStructTaskPO::getKnowledgeId, graphStructTaskPO.getKnowledgeId())
                .likeIfPresent(GraphStructTaskPO::getName, graphStructTaskPO.getName())
                .eqIfPresent(GraphStructTaskPO::getStatus, graphStructTaskPO.getStatus())
                .eqIfPresent(GraphStructTaskPO::getPublishStatus, graphStructTaskPO.getPublishStatus())
                .eqIfPresent(GraphStructTaskPO::getPublishTime, graphStructTaskPO.getPublishTime())
                .eqIfPresent(GraphStructTaskPO::getPublisherId, graphStructTaskPO.getPublisherId())
                .eqIfPresent(GraphStructTaskPO::getPublishBy, graphStructTaskPO.getPublishBy())
                .eqIfPresent(GraphStructTaskPO::getDatasourceId, graphStructTaskPO.getDatasourceId())
                .likeIfPresent(GraphStructTaskPO::getDatasourceName, graphStructTaskPO.getDatasourceName())
                .eqIfPresent(GraphStructTaskPO::getCreateTime, graphStructTaskPO.getCreateTime())
                .orderByAsc(GraphStructTaskPO::getCreateTime);
    }

}
