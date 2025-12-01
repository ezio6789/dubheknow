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
import com.insmess.knowledge.module.graph.convert.extract.GraphUnstructTaskDocRelConvert;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskDocRelPO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphUnstructTaskDocRelMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphUnstructTaskDocRelService;
/**
 * 任务文件关联Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphUnstructTaskDocRelServiceImpl  extends ServiceImpl<GraphUnstructTaskDocRelMapper,GraphUnstructTaskDocRelPO> implements GraphUnstructTaskDocRelService {
    @Resource
    private GraphUnstructTaskDocRelMapper graphUnstructTaskDocRelMapper;

    @Override
    public Page<GraphUnstructTaskDocRelPO> pageGraphUnstructTaskDocRel(GraphUnstructTaskDocRelPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphUnstructTaskDocRelPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphUnstructTaskDocRelPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphUnstructTaskDocRelMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphUnstructTaskDocRel(GraphUnstructTaskDocRelSaveReqVO saveReqVO) {
        GraphUnstructTaskDocRelPO graphUnstructTaskDocRelPO = GraphUnstructTaskDocRelConvert.INSTANCE.convertToPO(saveReqVO);
        graphUnstructTaskDocRelMapper.insert(graphUnstructTaskDocRelPO);
        return graphUnstructTaskDocRelPO.getId();
    }

    @Override
    public int updateGraphUnstructTaskDocRel(GraphUnstructTaskDocRelSaveReqVO updateReqVO) {
        GraphUnstructTaskDocRelPO updateObj = GraphUnstructTaskDocRelConvert.INSTANCE.convertToPO(updateReqVO);
        return graphUnstructTaskDocRelMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphUnstructTaskDocRel(Collection<Long> idList) {
        return graphUnstructTaskDocRelMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphUnstructTaskDocRelPO getGraphUnstructTaskDocRelById(Long id) {
        return graphUnstructTaskDocRelMapper.selectById(id);
    }

    @Override
    public List<GraphUnstructTaskDocRelPO> listGraphUnstructTaskDocRel() {
        return graphUnstructTaskDocRelMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphUnstructTaskDocRelPO> mapGraphUnstructTaskDocRel() {
        List<GraphUnstructTaskDocRelPO> graphUnstructTaskDocRelList = graphUnstructTaskDocRelMapper.selectList(null);
        return graphUnstructTaskDocRelList.stream()
                .collect(Collectors.toMap(
                        GraphUnstructTaskDocRelPO::getId,
                        graphUnstructTaskDocRelPO -> graphUnstructTaskDocRelPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入任务文件关联数据
         *
         * @param importExcelList 任务文件关联数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphUnstructTaskDocRel(List<GraphUnstructTaskDocRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphUnstructTaskDocRelRespVO respVO : importExcelList) {
                try {
                    GraphUnstructTaskDocRelPO graphUnstructTaskDocRelPO = BeanUtils.toBean(respVO, GraphUnstructTaskDocRelPO.class);
                    Long graphUnstructTaskDocRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphUnstructTaskDocRelId != null) {
                            GraphUnstructTaskDocRelPO existingGraphUnstructTaskDocRel = graphUnstructTaskDocRelMapper.selectById(graphUnstructTaskDocRelId);
                            if (existingGraphUnstructTaskDocRel != null) {
                                graphUnstructTaskDocRelMapper.updateById(graphUnstructTaskDocRelPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphUnstructTaskDocRelId + " 的任务文件关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphUnstructTaskDocRelId + " 的任务文件关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphUnstructTaskDocRelPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphUnstructTaskDocRelId);
                        GraphUnstructTaskDocRelPO existingGraphUnstructTaskDocRel = graphUnstructTaskDocRelMapper.selectOne(queryWrapper);
                        if (existingGraphUnstructTaskDocRel == null) {
                            graphUnstructTaskDocRelMapper.insert(graphUnstructTaskDocRelPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphUnstructTaskDocRelId + " 的任务文件关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphUnstructTaskDocRelId + " 的任务文件关联记录已存在。");
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

    private LambdaQueryWrapperX<GraphUnstructTaskDocRelPO> queryCondition(GraphUnstructTaskDocRelPageReqVO pageReqVO) {
        GraphUnstructTaskDocRelPO graphUnstructTaskDocRelPO = GraphUnstructTaskDocRelConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphUnstructTaskDocRelPO>()
                .eqIfPresent(GraphUnstructTaskDocRelPO::getTaskId, graphUnstructTaskDocRelPO.getTaskId())
                .eqIfPresent(GraphUnstructTaskDocRelPO::getDocId, graphUnstructTaskDocRelPO.getDocId())
                .likeIfPresent(GraphUnstructTaskDocRelPO::getDocName, graphUnstructTaskDocRelPO.getDocName())
                .eqIfPresent(GraphUnstructTaskDocRelPO::getCreateTime, graphUnstructTaskDocRelPO.getCreateTime())
                .orderByAsc(GraphUnstructTaskDocRelPO::getCreateTime);
    }

}
