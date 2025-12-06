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
import com.insmess.knowledge.module.graph.convert.ontology.GraphAttributeMappingConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphAttributeMappingPO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphAttributeMappingMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphAttributeMappingService;
/**
 * 属性映射Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphAttributeMappingServiceImpl  extends ServiceImpl<GraphAttributeMappingMapper,GraphAttributeMappingPO> implements GraphAttributeMappingService {
    @Resource
    private GraphAttributeMappingMapper graphAttributeMappingMapper;

    @Override
    public List<GraphAttributeMappingPO> list(GraphAttributeMappingPageReqVO pageReqVO) {
        return list(queryCondition(pageReqVO));
    }

    @Override
    public Page<GraphAttributeMappingPO> pageGraphAttributeMapping(GraphAttributeMappingPageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphAttributeMappingPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphAttributeMappingPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphAttributeMappingMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphAttributeMapping(GraphAttributeMappingSaveReqVO saveReqVO) {
        GraphAttributeMappingPO graphAttributeMappingPO = GraphAttributeMappingConvert.INSTANCE.convertToPO(saveReqVO);
        graphAttributeMappingMapper.insert(graphAttributeMappingPO);
        return graphAttributeMappingPO.getId();
    }

    @Override
    public int updateGraphAttributeMapping(GraphAttributeMappingSaveReqVO updateReqVO) {
        GraphAttributeMappingPO updateObj = GraphAttributeMappingConvert.INSTANCE.convertToPO(updateReqVO);
        return graphAttributeMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphAttributeMapping(Collection<Long> idList) {
        return graphAttributeMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphAttributeMappingPO getGraphAttributeMappingById(Long id) {
        return graphAttributeMappingMapper.selectById(id);
    }

    @Override
    public List<GraphAttributeMappingPO> listGraphAttributeMapping() {
        return graphAttributeMappingMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphAttributeMappingPO> mapGraphAttributeMapping() {
        List<GraphAttributeMappingPO> graphAttributeMappingList = graphAttributeMappingMapper.selectList(null);
        return graphAttributeMappingList.stream()
                .collect(Collectors.toMap(
                        GraphAttributeMappingPO::getId,
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
        public String importGraphAttributeMapping(List<GraphAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphAttributeMappingRespVO respVO : importExcelList) {
                try {
                    GraphAttributeMappingPO graphAttributeMappingPO = BeanUtils.toBean(respVO, GraphAttributeMappingPO.class);
                    Long graphAttributeMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphAttributeMappingId != null) {
                            GraphAttributeMappingPO existingGraphAttributeMapping = graphAttributeMappingMapper.selectById(graphAttributeMappingId);
                            if (existingGraphAttributeMapping != null) {
                                graphAttributeMappingMapper.updateById(graphAttributeMappingPO);
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
                        QueryWrapper<GraphAttributeMappingPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphAttributeMappingId);
                        GraphAttributeMappingPO existingGraphAttributeMapping = graphAttributeMappingMapper.selectOne(queryWrapper);
                        if (existingGraphAttributeMapping == null) {
                            graphAttributeMappingMapper.insert(graphAttributeMappingPO);
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
    public List<GraphAttributeMappingPO> listByTaskId(Long id) {
        GraphAttributeMappingPageReqVO params = new GraphAttributeMappingPageReqVO();
        params.setTaskId(id);
        return list(queryCondition(params));
    }

    private LambdaQueryWrapperX<GraphAttributeMappingPO> queryCondition(GraphAttributeMappingPageReqVO pageReqVO) {
        GraphAttributeMappingPO graphAttributeMappingPO = GraphAttributeMappingConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphAttributeMappingPO>()
                .eqIfPresent(GraphAttributeMappingPO::getTaskId, graphAttributeMappingPO.getTaskId())
                .likeIfPresent(GraphAttributeMappingPO::getTableName, graphAttributeMappingPO.getTableName())
                .eqIfPresent(GraphAttributeMappingPO::getTableComment, graphAttributeMappingPO.getTableComment())
                .likeIfPresent(GraphAttributeMappingPO::getFieldName, graphAttributeMappingPO.getFieldName())
                .eqIfPresent(GraphAttributeMappingPO::getFieldComment, graphAttributeMappingPO.getFieldComment())
                .eqIfPresent(GraphAttributeMappingPO::getAttributeId, graphAttributeMappingPO.getAttributeId())
                .likeIfPresent(GraphAttributeMappingPO::getAttributeName, graphAttributeMappingPO.getAttributeName())
                .eqIfPresent(GraphAttributeMappingPO::getCreateTime, graphAttributeMappingPO.getCreateTime())
                .orderByAsc(GraphAttributeMappingPO::getCreateTime);
    }

}
