package com.insmess.knowledge.module.graph.service.extract.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskConceptMappingConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingRespVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskConceptMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphStructTaskConceptMappingMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskConceptMappingService;
/**
 * 概念映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphStructTaskConceptMappingServiceImpl extends ServiceImpl<GraphStructTaskConceptMappingMapper, GraphStructTaskConceptMappingPO> implements GraphStructTaskConceptMappingService {
    @Resource
    private GraphStructTaskConceptMappingMapper graphStructTaskConceptMappingMapper;

    @Override
    public List<GraphStructTaskConceptMappingPO> listByTaskId(Long taskId) {
        return list(new QueryWrapper<GraphStructTaskConceptMappingPO>().lambda().eq(GraphStructTaskConceptMappingPO::getTaskId, taskId));
    }

    @Override
    public Page<GraphStructTaskConceptMappingPO> pageGraphConceptMapping(GraphStructTaskConceptMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphStructTaskConceptMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphStructTaskConceptMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphStructTaskConceptMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphConceptMapping(GraphStructTaskConceptMappingSaveReqVO saveReqVO) {
        GraphStructTaskConceptMappingPO graphStructTaskConceptMappingPO = GraphStructTaskConceptMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphStructTaskConceptMappingMapper.insert(graphStructTaskConceptMappingPO);
        return graphStructTaskConceptMappingPO.getId();
    }

    @Override
    public int updateGraphConceptMapping(GraphStructTaskConceptMappingSaveReqVO updateReqVO) {
        GraphStructTaskConceptMappingPO updateObj = GraphStructTaskConceptMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphStructTaskConceptMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphConceptMapping(Collection<Long> idList) {
        return graphStructTaskConceptMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphStructTaskConceptMappingPO getGraphConceptMappingById(Long id) {
        return graphStructTaskConceptMappingMapper.selectById(id);
    }

    @Override
    public List<GraphStructTaskConceptMappingPO> listGraphConceptMapping() {
        return graphStructTaskConceptMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphStructTaskConceptMappingPO> mapGraphConceptMapping() {
        List<GraphStructTaskConceptMappingPO> graphConceptMappingList = graphStructTaskConceptMappingMapper.selectList(null);
        return graphConceptMappingList.stream()
                .collect(Collectors.toMap(
                        GraphStructTaskConceptMappingPO::getId,
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
        public String importGraphConceptMapping(List<GraphStructTaskConceptMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphStructTaskConceptMappingRespVO respVO : importExcelList) {
                try {
                    GraphStructTaskConceptMappingPO graphStructTaskConceptMappingPO = BeanUtils.toBean(respVO, GraphStructTaskConceptMappingPO.class);
                    Long graphConceptMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphConceptMappingId != null) {
                            GraphStructTaskConceptMappingPO existingGraphConceptMapping = graphStructTaskConceptMappingMapper.selectById(graphConceptMappingId);
                            if (existingGraphConceptMapping != null) {
                                graphStructTaskConceptMappingMapper.updateById(graphStructTaskConceptMappingPO);
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
                        QueryWrapper<GraphStructTaskConceptMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphConceptMappingId);
                        GraphStructTaskConceptMappingPO existingGraphConceptMapping = graphStructTaskConceptMappingMapper.selectOne(queryWrapper);
                        if (existingGraphConceptMapping == null) {
                            graphStructTaskConceptMappingMapper.insert(graphStructTaskConceptMappingPO);
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

    private LambdaQueryWrapperX<GraphStructTaskConceptMappingPO> queryCondition(GraphStructTaskConceptMappingPageReqVO pageReqVO) {
        GraphStructTaskConceptMappingPO graphStructTaskConceptMappingPO = GraphStructTaskConceptMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphStructTaskConceptMappingPO>()
                .eqIfPresent(GraphStructTaskConceptMappingPO::getTaskId, graphStructTaskConceptMappingPO.getTaskId())
                .likeIfPresent(GraphStructTaskConceptMappingPO::getTableName, graphStructTaskConceptMappingPO.getTableName())
                .eqIfPresent(GraphStructTaskConceptMappingPO::getTableComment, graphStructTaskConceptMappingPO.getTableComment())
                .eqIfPresent(GraphStructTaskConceptMappingPO::getEntityNameField, graphStructTaskConceptMappingPO.getEntityNameField())
                .eqIfPresent(GraphStructTaskConceptMappingPO::getConceptId, graphStructTaskConceptMappingPO.getConceptId())
                .likeIfPresent(GraphStructTaskConceptMappingPO::getConceptName, graphStructTaskConceptMappingPO.getConceptName())
                .eqIfPresent(GraphStructTaskConceptMappingPO::getCreateTime, graphStructTaskConceptMappingPO.getCreateTime())
                .orderByAsc(GraphStructTaskConceptMappingPO::getCreateTime);
    }

}
