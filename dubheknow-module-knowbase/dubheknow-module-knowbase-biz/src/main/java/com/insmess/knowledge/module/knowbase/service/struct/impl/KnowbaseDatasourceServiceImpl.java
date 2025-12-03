package com.insmess.knowledge.module.knowbase.service.struct.impl;

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
import com.insmess.knowledge.module.knowbase.convert.struct.KnowbaseDatasourceConvert;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourcePageReqVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceRespVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.struct.KnowbaseDatasourcePO;
import com.insmess.knowledge.module.knowbase.dao.mapper.struct.KnowbaseDatasourceMapper;
import com.insmess.knowledge.module.knowbase.service.struct.KnowbaseDatasourceService;
/**
 * 数据源Service业务层处理
 *
 * @author insmess
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KnowbaseDatasourceServiceImpl  extends ServiceImpl<KnowbaseDatasourceMapper,KnowbaseDatasourcePO> implements KnowbaseDatasourceService {
    @Resource
    private KnowbaseDatasourceMapper knowbaseDatasourceMapper;

    @Override
    public Page<KnowbaseDatasourcePO> pageKnowbaseDatasource(KnowbaseDatasourcePageReqVO pageReqVO) {
        LambdaQueryWrapperX<KnowbaseDatasourcePO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<KnowbaseDatasourcePO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return knowbaseDatasourceMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveKnowbaseDatasource(KnowbaseDatasourceSaveReqVO saveReqVO) {
        KnowbaseDatasourcePO knowbaseDatasourcePO = KnowbaseDatasourceConvert.INSTANCE.convertToPO(saveReqVO);
        knowbaseDatasourceMapper.insert(knowbaseDatasourcePO);
        return knowbaseDatasourcePO.getId();
    }

    @Override
    public int updateKnowbaseDatasource(KnowbaseDatasourceSaveReqVO updateReqVO) {
        KnowbaseDatasourcePO updateObj = KnowbaseDatasourceConvert.INSTANCE.convertToPO(updateReqVO);
        return knowbaseDatasourceMapper.updateById(updateObj);
    }
    @Override
    public int removeKnowbaseDatasource(Collection<Long> idList) {
        return knowbaseDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public KnowbaseDatasourcePO getKnowbaseDatasourceById(Long id) {
        return knowbaseDatasourceMapper.selectById(id);
    }

    @Override
    public List<KnowbaseDatasourcePO> listKnowbaseDatasource() {
        return knowbaseDatasourceMapper.selectList(null);
    }

    @Override
    public Map<Long, KnowbaseDatasourcePO> mapKnowbaseDatasource() {
        List<KnowbaseDatasourcePO> knowbaseDatasourceList = knowbaseDatasourceMapper.selectList(null);
        return knowbaseDatasourceList.stream()
                .collect(Collectors.toMap(
                        KnowbaseDatasourcePO::getId,
                        knowbaseDatasourcePO -> knowbaseDatasourcePO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据源数据
         *
         * @param importExcelList 数据源数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKnowbaseDatasource(List<KnowbaseDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KnowbaseDatasourceRespVO respVO : importExcelList) {
                try {
                    KnowbaseDatasourcePO knowbaseDatasourcePO = BeanUtils.toBean(respVO, KnowbaseDatasourcePO.class);
                    Long knowbaseDatasourceId = respVO.getId();
                    if (isUpdateSupport) {
                        if (knowbaseDatasourceId != null) {
                            KnowbaseDatasourcePO existingKnowbaseDatasource = knowbaseDatasourceMapper.selectById(knowbaseDatasourceId);
                            if (existingKnowbaseDatasource != null) {
                                knowbaseDatasourceMapper.updateById(knowbaseDatasourcePO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + knowbaseDatasourceId + " 的数据源记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + knowbaseDatasourceId + " 的数据源记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KnowbaseDatasourcePO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", knowbaseDatasourceId);
                        KnowbaseDatasourcePO existingKnowbaseDatasource = knowbaseDatasourceMapper.selectOne(queryWrapper);
                        if (existingKnowbaseDatasource == null) {
                            knowbaseDatasourceMapper.insert(knowbaseDatasourcePO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + knowbaseDatasourceId + " 的数据源记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + knowbaseDatasourceId + " 的数据源记录已存在。");
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

    private LambdaQueryWrapperX<KnowbaseDatasourcePO> queryCondition(KnowbaseDatasourcePageReqVO pageReqVO) {
        KnowbaseDatasourcePO knowbaseDatasourcePO = KnowbaseDatasourceConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<KnowbaseDatasourcePO>()
                .likeIfPresent(KnowbaseDatasourcePO::getDatasourceName, knowbaseDatasourcePO.getDatasourceName())
                .eqIfPresent(KnowbaseDatasourcePO::getDatasourceType, knowbaseDatasourcePO.getDatasourceType())
                .eqIfPresent(KnowbaseDatasourcePO::getDatasourceConfig, knowbaseDatasourcePO.getDatasourceConfig())
                .eqIfPresent(KnowbaseDatasourcePO::getIp, knowbaseDatasourcePO.getIp())
                .eqIfPresent(KnowbaseDatasourcePO::getPort, knowbaseDatasourcePO.getPort())
                .eqIfPresent(KnowbaseDatasourcePO::getDescription, knowbaseDatasourcePO.getDescription())
                .eqIfPresent(KnowbaseDatasourcePO::getCreateTime, knowbaseDatasourcePO.getCreateTime())
                .orderByAsc(KnowbaseDatasourcePO::getCreateTime);
    }

}
