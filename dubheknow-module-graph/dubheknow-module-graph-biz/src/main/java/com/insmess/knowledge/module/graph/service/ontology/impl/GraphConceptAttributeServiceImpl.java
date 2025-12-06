package com.insmess.knowledge.module.graph.service.ontology.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptAttributePO;
import com.insmess.knowledge.module.graph.service.ontology.GraphConceptAttributeService;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeRespVO;
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
import com.insmess.knowledge.module.graph.convert.ontology.GraphConceptAttributeConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptAttributeSaveReqVO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphConceptAttributeMapper;

/**
 * 概念属性Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphConceptAttributeServiceImpl extends ServiceImpl<GraphConceptAttributeMapper, GraphConceptAttributePO> implements GraphConceptAttributeService {
    @Resource
    private GraphConceptAttributeMapper graphConceptAttributeMapper;

    @Override
    public Page<GraphConceptAttributePO> pageGraphConceptAttribute(GraphConceptAttributePageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphConceptAttributePO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphConceptAttributePO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphConceptAttributeMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphConceptAttribute(GraphConceptAttributeSaveReqVO saveReqVO) {
        GraphConceptAttributePO graphConceptAttributePO = GraphConceptAttributeConvert.INSTANCE.convertToPO(saveReqVO);
        graphConceptAttributeMapper.insert(graphConceptAttributePO);
        return graphConceptAttributePO.getId();
    }

    @Override
    public int updateGraphConceptAttribute(GraphConceptAttributeSaveReqVO updateReqVO) {
        GraphConceptAttributePO updateObj = GraphConceptAttributeConvert.INSTANCE.convertToPO(updateReqVO);
        return graphConceptAttributeMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphConceptAttribute(Collection<Long> idList) {
        return graphConceptAttributeMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphConceptAttributePO getGraphConceptAttributeById(Long id) {
        return graphConceptAttributeMapper.selectById(id);
    }

    @Override
    public List<GraphConceptAttributePO> listGraphConceptAttribute() {
        return graphConceptAttributeMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphConceptAttributePO> mapGraphConceptAttribute() {
        List<GraphConceptAttributePO> graphConceptAttributeList = graphConceptAttributeMapper.selectList(null);
        return graphConceptAttributeList.stream()
                .collect(Collectors.toMap(
                        GraphConceptAttributePO::getId,
                        graphConceptAttributePO -> graphConceptAttributePO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入概念属性数据
         *
         * @param importExcelList 概念属性数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importGraphConceptAttribute(List<GraphConceptAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphConceptAttributeRespVO respVO : importExcelList) {
                try {
                    GraphConceptAttributePO graphConceptAttributePO = BeanUtils.toBean(respVO, GraphConceptAttributePO.class);
                    Long graphConceptAttributeId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphConceptAttributeId != null) {
                            GraphConceptAttributePO existingGraphConceptAttribute = graphConceptAttributeMapper.selectById(graphConceptAttributeId);
                            if (existingGraphConceptAttribute != null) {
                                graphConceptAttributeMapper.updateById(graphConceptAttributePO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphConceptAttributeId + " 的概念属性记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphConceptAttributeId + " 的概念属性记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphConceptAttributePO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphConceptAttributeId);
                        GraphConceptAttributePO existingGraphConceptAttribute = graphConceptAttributeMapper.selectOne(queryWrapper);
                        if (existingGraphConceptAttribute == null) {
                            graphConceptAttributeMapper.insert(graphConceptAttributePO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphConceptAttributeId + " 的概念属性记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphConceptAttributeId + " 的概念属性记录已存在。");
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

    private LambdaQueryWrapperX<GraphConceptAttributePO> queryCondition(GraphConceptAttributePageReqVO pageReqVO) {
        GraphConceptAttributePO graphConceptAttributePO = GraphConceptAttributeConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphConceptAttributePO>()
                .eqIfPresent(GraphConceptAttributePO::getConceptId, graphConceptAttributePO.getConceptId())
                .likeIfPresent(GraphConceptAttributePO::getConceptName, graphConceptAttributePO.getConceptName())
                .likeIfPresent(GraphConceptAttributePO::getName, graphConceptAttributePO.getName())
                .eqIfPresent(GraphConceptAttributePO::getNameCode, graphConceptAttributePO.getNameCode())
                .eqIfPresent(GraphConceptAttributePO::getRequireFlag, graphConceptAttributePO.getRequireFlag())
                .eqIfPresent(GraphConceptAttributePO::getDataType, graphConceptAttributePO.getDataType())
                .eqIfPresent(GraphConceptAttributePO::getMultipleFlag, graphConceptAttributePO.getMultipleFlag())
                .eqIfPresent(GraphConceptAttributePO::getValidateType, graphConceptAttributePO.getValidateType())
                .eqIfPresent(GraphConceptAttributePO::getMinValue, graphConceptAttributePO.getMinValue())
                .eqIfPresent(GraphConceptAttributePO::getMaxValue, graphConceptAttributePO.getMaxValue())
                .eqIfPresent(GraphConceptAttributePO::getCreateTime, graphConceptAttributePO.getCreateTime())
                .orderByAsc(GraphConceptAttributePO::getCreateTime);
    }

}
