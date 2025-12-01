package com.insmess.knowledge.module.graph.service.manager.impl;

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
import com.insmess.knowledge.module.graph.convert.manager.GraphKnowledgeConvert;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgePageReqVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeRespVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.manager.GraphKnowledgePO;
import com.insmess.knowledge.module.graph.dao.mapper.manager.GraphKnowledgeMapper;
import com.insmess.knowledge.module.graph.service.manager.GraphKnowledgeService;
/**
 * 知识图谱Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphKnowledgeServiceImpl  extends ServiceImpl<GraphKnowledgeMapper,GraphKnowledgePO> implements GraphKnowledgeService {
    @Resource
    private GraphKnowledgeMapper graphKnowledgeMapper;

    @Override
    public Page<GraphKnowledgePO> pageGraphKnowledge(GraphKnowledgePageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphKnowledgePO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphKnowledgePO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphKnowledgeMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphKnowledge(GraphKnowledgeSaveReqVO saveReqVO) {
        GraphKnowledgePO graphKnowledgePO = GraphKnowledgeConvert.INSTANCE.convertToPO(saveReqVO);
        graphKnowledgeMapper.insert(graphKnowledgePO);
        return graphKnowledgePO.getId();
    }

    @Override
    public int updateGraphKnowledge(GraphKnowledgeSaveReqVO updateReqVO) {
        GraphKnowledgePO updateObj = GraphKnowledgeConvert.INSTANCE.convertToPO(updateReqVO);
        return graphKnowledgeMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphKnowledge(Collection<Long> idList) {
        return graphKnowledgeMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphKnowledgePO getGraphKnowledgeById(Long id) {
        return graphKnowledgeMapper.selectById(id);
    }

    @Override
    public List<GraphKnowledgePO> listGraphKnowledge() {
        return graphKnowledgeMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphKnowledgePO> mapGraphKnowledge() {
        List<GraphKnowledgePO> graphKnowledgeList = graphKnowledgeMapper.selectList(null);
        return graphKnowledgeList.stream()
                .collect(Collectors.toMap(
                        GraphKnowledgePO::getId,
                        graphKnowledgePO -> graphKnowledgePO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入知识图谱数据
         *
         * @param importExcelList 知识图谱数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphKnowledge(List<GraphKnowledgeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphKnowledgeRespVO respVO : importExcelList) {
                try {
                    GraphKnowledgePO graphKnowledgePO = BeanUtils.toBean(respVO, GraphKnowledgePO.class);
                    Long graphKnowledgeId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphKnowledgeId != null) {
                            GraphKnowledgePO existingGraphKnowledge = graphKnowledgeMapper.selectById(graphKnowledgeId);
                            if (existingGraphKnowledge != null) {
                                graphKnowledgeMapper.updateById(graphKnowledgePO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphKnowledgeId + " 的知识图谱记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphKnowledgeId + " 的知识图谱记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphKnowledgePO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphKnowledgeId);
                        GraphKnowledgePO existingGraphKnowledge = graphKnowledgeMapper.selectOne(queryWrapper);
                        if (existingGraphKnowledge == null) {
                            graphKnowledgeMapper.insert(graphKnowledgePO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphKnowledgeId + " 的知识图谱记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphKnowledgeId + " 的知识图谱记录已存在。");
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

    private LambdaQueryWrapperX<GraphKnowledgePO> queryCondition(GraphKnowledgePageReqVO pageReqVO) {
        GraphKnowledgePO graphKnowledgePO = GraphKnowledgeConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphKnowledgePO>()
                .likeIfPresent(GraphKnowledgePO::getName, graphKnowledgePO.getName())
                .eqIfPresent(GraphKnowledgePO::getDescription, graphKnowledgePO.getDescription())
                .eqIfPresent(GraphKnowledgePO::getCreateTime, graphKnowledgePO.getCreateTime())
                .orderByAsc(GraphKnowledgePO::getCreateTime);
    }

}
