package com.insmess.knowledge.module.graph.service.extract.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.convert.extract.GraphStructTaskAttributeMappingConvert;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskAttributeMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingSaveReqVO;
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
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.dao.mapper.extract.GraphStructTaskAttributeMappingMapper;
import com.insmess.knowledge.module.graph.service.extract.GraphStructTaskAttributeMappingService;
/**
 * 属性映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphStructTaskAttributeMappingServiceImpl extends ServiceImpl<GraphStructTaskAttributeMappingMapper, GraphStructTaskAttributeMappingPO> implements GraphStructTaskAttributeMappingService {
    @Resource
    private GraphStructTaskAttributeMappingMapper graphStructTaskAttributeMappingMapper;

    @Override
    public List<GraphStructTaskAttributeMappingPO> list(GraphStructTaskAttributeMappingPageReqVO pageReqVO) {
        return list(queryCondition(pageReqVO));
    }

    @Override
    public Page<GraphStructTaskAttributeMappingPO> pageGraphAttributeMapping(GraphStructTaskAttributeMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphStructTaskAttributeMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphStructTaskAttributeMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphStructTaskAttributeMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphAttributeMapping(GraphStructTaskAttributeMappingSaveReqVO saveReqVO) {
        GraphStructTaskAttributeMappingPO graphStructTaskAttributeMappingPO = GraphStructTaskAttributeMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphStructTaskAttributeMappingMapper.insert(graphStructTaskAttributeMappingPO);
        return graphStructTaskAttributeMappingPO.getId();
    }

    @Override
    public int updateGraphAttributeMapping(GraphStructTaskAttributeMappingSaveReqVO updateReqVO) {
        GraphStructTaskAttributeMappingPO updateObj = GraphStructTaskAttributeMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphStructTaskAttributeMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphAttributeMapping(Collection<Long> idList) {
        return graphStructTaskAttributeMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphStructTaskAttributeMappingPO getGraphAttributeMappingById(Long id) {
        return graphStructTaskAttributeMappingMapper.selectById(id);
    }

    @Override
    public List<GraphStructTaskAttributeMappingPO> listGraphAttributeMapping() {
        return graphStructTaskAttributeMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphStructTaskAttributeMappingPO> mapGraphAttributeMapping() {
        List<GraphStructTaskAttributeMappingPO> graphAttributeMappingList = graphStructTaskAttributeMappingMapper.selectList(null);
        return graphAttributeMappingList.stream()
                .collect(Collectors.toMap(
                        GraphStructTaskAttributeMappingPO::getId,
                        graphAttributeMappingPO -> graphAttributeMappingPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入属性映射数据
         *
         * @param importExcelList 属性映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphAttributeMapping(List<GraphStructTaskAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphStructTaskAttributeMappingRespVO respVO : importExcelList) {
                try {
                    GraphStructTaskAttributeMappingPO graphStructTaskAttributeMappingPO = BeanUtils.toBean(respVO, GraphStructTaskAttributeMappingPO.class);
                    Long graphAttributeMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphAttributeMappingId != null) {
                            GraphStructTaskAttributeMappingPO existingGraphAttributeMapping = graphStructTaskAttributeMappingMapper.selectById(graphAttributeMappingId);
                            if (existingGraphAttributeMapping != null) {
                                graphStructTaskAttributeMappingMapper.updateById(graphStructTaskAttributeMappingPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphAttributeMappingId + " 的属性映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphAttributeMappingId + " 的属性映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphStructTaskAttributeMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphAttributeMappingId);
                        GraphStructTaskAttributeMappingPO existingGraphAttributeMapping = graphStructTaskAttributeMappingMapper.selectOne(queryWrapper);
                        if (existingGraphAttributeMapping == null) {
                            graphStructTaskAttributeMappingMapper.insert(graphStructTaskAttributeMappingPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphAttributeMappingId + " 的属性映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphAttributeMappingId + " 的属性映射记录已存在。");
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
    public List<GraphStructTaskAttributeMappingPO> listByTaskId(Long id) {
        GraphStructTaskAttributeMappingPageReqVO params = new GraphStructTaskAttributeMappingPageReqVO();
        params.setTaskId(id);
        return list(queryCondition(params));
    }

    private LambdaQueryWrapperX<GraphStructTaskAttributeMappingPO> queryCondition(GraphStructTaskAttributeMappingPageReqVO pageReqVO) {
        GraphStructTaskAttributeMappingPO graphStructTaskAttributeMappingPO = GraphStructTaskAttributeMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphStructTaskAttributeMappingPO>()
                .eqIfPresent(GraphStructTaskAttributeMappingPO::getTaskId, graphStructTaskAttributeMappingPO.getTaskId())
                .likeIfPresent(GraphStructTaskAttributeMappingPO::getTableName, graphStructTaskAttributeMappingPO.getTableName())
                .eqIfPresent(GraphStructTaskAttributeMappingPO::getTableComment, graphStructTaskAttributeMappingPO.getTableComment())
                .likeIfPresent(GraphStructTaskAttributeMappingPO::getFieldName, graphStructTaskAttributeMappingPO.getFieldName())
                .eqIfPresent(GraphStructTaskAttributeMappingPO::getFieldComment, graphStructTaskAttributeMappingPO.getFieldComment())
                .eqIfPresent(GraphStructTaskAttributeMappingPO::getAttributeId, graphStructTaskAttributeMappingPO.getAttributeId())
                .likeIfPresent(GraphStructTaskAttributeMappingPO::getAttributeName, graphStructTaskAttributeMappingPO.getAttributeName())
                .eqIfPresent(GraphStructTaskAttributeMappingPO::getCreateTime, graphStructTaskAttributeMappingPO.getCreateTime())
                .orderByAsc(GraphStructTaskAttributeMappingPO::getCreateTime);
    }

}
