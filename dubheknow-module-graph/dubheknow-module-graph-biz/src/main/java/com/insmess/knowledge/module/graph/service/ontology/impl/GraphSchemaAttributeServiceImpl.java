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
import com.insmess.knowledge.module.graph.convert.ontology.GraphSchemaAttributeConvert;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributePageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeRespVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphSchemaAttributeSaveReqVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphSchemaAttributePO;
import com.insmess.knowledge.module.graph.dao.mapper.ontology.GraphSchemaAttributeMapper;
import com.insmess.knowledge.module.graph.service.ontology.GraphSchemaAttributeService;
/**
 * 概念属性Service业务层处理
 *
 * @author insmess
 * @date 2025-11-29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GraphSchemaAttributeServiceImpl  extends ServiceImpl<GraphSchemaAttributeMapper,GraphSchemaAttributePO> implements GraphSchemaAttributeService {
    @Resource
    private GraphSchemaAttributeMapper graphSchemaAttributeMapper;

    @Override
    public Page<GraphSchemaAttributePO> pageGraphSchemaAttribute(GraphSchemaAttributePageReqVO pageReqVO) {
        LambdaQueryWrapperX<GraphSchemaAttributePO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<GraphSchemaAttributePO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return graphSchemaAttributeMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveGraphSchemaAttribute(GraphSchemaAttributeSaveReqVO saveReqVO) {
        GraphSchemaAttributePO graphSchemaAttributePO = GraphSchemaAttributeConvert.INSTANCE.convertToPO(saveReqVO);
        graphSchemaAttributeMapper.insert(graphSchemaAttributePO);
        return graphSchemaAttributePO.getId();
    }

    @Override
    public int updateGraphSchemaAttribute(GraphSchemaAttributeSaveReqVO updateReqVO) {
        GraphSchemaAttributePO updateObj = GraphSchemaAttributeConvert.INSTANCE.convertToPO(updateReqVO);
        return graphSchemaAttributeMapper.updateById(updateObj);
    }
    @Override
    public int removeGraphSchemaAttribute(Collection<Long> idList) {
        return graphSchemaAttributeMapper.deleteBatchIds(idList);
    }

    @Override
    public GraphSchemaAttributePO getGraphSchemaAttributeById(Long id) {
        return graphSchemaAttributeMapper.selectById(id);
    }

    @Override
    public List<GraphSchemaAttributePO> listGraphSchemaAttribute() {
        return graphSchemaAttributeMapper.selectList(null);
    }

    @Override
    public Map<Long, GraphSchemaAttributePO> mapGraphSchemaAttribute() {
        List<GraphSchemaAttributePO> graphSchemaAttributeList = graphSchemaAttributeMapper.selectList(null);
        return graphSchemaAttributeList.stream()
                .collect(Collectors.toMap(
                        GraphSchemaAttributePO::getId,
                        graphSchemaAttributePO -> graphSchemaAttributePO,
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
        public String importGraphSchemaAttribute(List<GraphSchemaAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (GraphSchemaAttributeRespVO respVO : importExcelList) {
                try {
                    GraphSchemaAttributePO graphSchemaAttributePO = BeanUtils.toBean(respVO, GraphSchemaAttributePO.class);
                    Long graphSchemaAttributeId = respVO.getId();
                    if (isUpdateSupport) {
                        if (graphSchemaAttributeId != null) {
                            GraphSchemaAttributePO existingGraphSchemaAttribute = graphSchemaAttributeMapper.selectById(graphSchemaAttributeId);
                            if (existingGraphSchemaAttribute != null) {
                                graphSchemaAttributeMapper.updateById(graphSchemaAttributePO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + graphSchemaAttributeId + " 的概念属性记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + graphSchemaAttributeId + " 的概念属性记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<GraphSchemaAttributePO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", graphSchemaAttributeId);
                        GraphSchemaAttributePO existingGraphSchemaAttribute = graphSchemaAttributeMapper.selectOne(queryWrapper);
                        if (existingGraphSchemaAttribute == null) {
                            graphSchemaAttributeMapper.insert(graphSchemaAttributePO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + graphSchemaAttributeId + " 的概念属性记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + graphSchemaAttributeId + " 的概念属性记录已存在。");
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

    private LambdaQueryWrapperX<GraphSchemaAttributePO> queryCondition(GraphSchemaAttributePageReqVO pageReqVO) {
        GraphSchemaAttributePO graphSchemaAttributePO = GraphSchemaAttributeConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<GraphSchemaAttributePO>()
                .eqIfPresent(GraphSchemaAttributePO::getSchemaId, graphSchemaAttributePO.getSchemaId())
                .likeIfPresent(GraphSchemaAttributePO::getSchemaName, graphSchemaAttributePO.getSchemaName())
                .likeIfPresent(GraphSchemaAttributePO::getName, graphSchemaAttributePO.getName())
                .eqIfPresent(GraphSchemaAttributePO::getNameCode, graphSchemaAttributePO.getNameCode())
                .eqIfPresent(GraphSchemaAttributePO::getRequireFlag, graphSchemaAttributePO.getRequireFlag())
                .eqIfPresent(GraphSchemaAttributePO::getDataType, graphSchemaAttributePO.getDataType())
                .eqIfPresent(GraphSchemaAttributePO::getMultipleFlag, graphSchemaAttributePO.getMultipleFlag())
                .eqIfPresent(GraphSchemaAttributePO::getValidateType, graphSchemaAttributePO.getValidateType())
                .eqIfPresent(GraphSchemaAttributePO::getMinValue, graphSchemaAttributePO.getMinValue())
                .eqIfPresent(GraphSchemaAttributePO::getMaxValue, graphSchemaAttributePO.getMaxValue())
                .eqIfPresent(GraphSchemaAttributePO::getCreateTime, graphSchemaAttributePO.getCreateTime())
                .orderByAsc(GraphSchemaAttributePO::getCreateTime);
    }

}
