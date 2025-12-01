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
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskTextConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskTextPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphUnstructTaskTextMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskTextService;
/**
 * 任务文件段落关联Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphUnstructTaskTextServiceImpl  extends ServiceImpl<GraphUnstructTaskTextMapper,GraphUnstructTaskTextPO> implements GraphUnstructTaskTextService {
    @Resource
    private GraphUnstructTaskTextMapper graphUnstructTaskTextMapper;

    @Override
    public Page<GraphUnstructTaskTextPO> pageGraphUnstructTaskText(GraphUnstructTaskTextPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphUnstructTaskTextPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphUnstructTaskTextPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphUnstructTaskTextMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphUnstructTaskText(GraphUnstructTaskTextSaveReqVO saveReqVO) {
        GraphUnstructTaskTextPO graphUnstructTaskTextPO = GraphUnstructTaskTextConvert.INSTANCE.convertToPO(saveReqVO);
        graphUnstructTaskTextMapper.insert(graphUnstructTaskTextPO);
        return graphUnstructTaskTextPO.getId();
    }

    @Override
    public int updateGraphUnstructTaskText(GraphUnstructTaskTextSaveReqVO updateReqVO) {
        GraphUnstructTaskTextPO updateObj = GraphUnstructTaskTextConvert.INSTANCE.convertToPO(updateReqVO);
        return graphUnstructTaskTextMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphUnstructTaskText(Collection<Long> idList) {
        return graphUnstructTaskTextMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphUnstructTaskTextPO getGraphUnstructTaskTextById(Long id) {
        return graphUnstructTaskTextMapper.selectById(id);
    }

    @Override
    public List<GraphUnstructTaskTextPO> listGraphUnstructTaskText() {
        return graphUnstructTaskTextMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphUnstructTaskTextPO> mapGraphUnstructTaskText() {
        List<GraphUnstructTaskTextPO> graphUnstructTaskTextList = graphUnstructTaskTextMapper.selectList(null);
        return graphUnstructTaskTextList.stream()
                .collect(Collectors.toMap(
                        GraphUnstructTaskTextPO::getId,
                        graphUnstructTaskTextPO -> graphUnstructTaskTextPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入任务文件段落关联数据
         *
         * @param importExcelList 任务文件段落关联数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphUnstructTaskText(List<GraphUnstructTaskTextRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphUnstructTaskTextRespVO respVO : importExcelList) {
                try {
                    GraphUnstructTaskTextPO graphUnstructTaskTextPO = BeanUtils.toBean(respVO, GraphUnstructTaskTextPO.class);
                    Long graphUnstructTaskTextId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphUnstructTaskTextId != null) {
                            GraphUnstructTaskTextPO existingGraphUnstructTaskText = graphUnstructTaskTextMapper.selectById(graphUnstructTaskTextId);
                            if (existingGraphUnstructTaskText != null) {
                                graphUnstructTaskTextMapper.updateById(graphUnstructTaskTextPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphUnstructTaskTextId + " 的任务文件段落关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphUnstructTaskTextId + " 的任务文件段落关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphUnstructTaskTextPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphUnstructTaskTextId);
                        GraphUnstructTaskTextPO existingGraphUnstructTaskText = graphUnstructTaskTextMapper.selectOne(queryWrapper);
                        if (existingGraphUnstructTaskText == null) {
                            graphUnstructTaskTextMapper.insert(graphUnstructTaskTextPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphUnstructTaskTextId + " 的任务文件段落关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphUnstructTaskTextId + " 的任务文件段落关联记录已存在。");
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

    private LambdaQueryWrapperX<GraphUnstructTaskTextPO> queryCondition(GraphUnstructTaskTextPageReqVO pageReqVO) {
        GraphUnstructTaskTextPO graphUnstructTaskTextPO = GraphUnstructTaskTextConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphUnstructTaskTextPO>()
                .eqIfPresent(GraphUnstructTaskTextPO::getTaskId, graphUnstructTaskTextPO.getTaskId())
                .eqIfPresent(GraphUnstructTaskTextPO::getDocId, graphUnstructTaskTextPO.getDocId())
                .eqIfPresent(GraphUnstructTaskTextPO::getParagraphIndex, graphUnstructTaskTextPO.getParagraphIndex())
                .eqIfPresent(GraphUnstructTaskTextPO::getText, graphUnstructTaskTextPO.getText())
                .eqIfPresent(GraphUnstructTaskTextPO::getCreateTime, graphUnstructTaskTextPO.getCreateTime())
                .orderByAsc(GraphUnstructTaskTextPO::getCreateTime);
    }

}
