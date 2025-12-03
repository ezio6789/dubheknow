package com.insmess.knowledge.module.knowbase.service.unstruct.impl;

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
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentDirConvert;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentDirSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentDirPO;
import com.insmess.knowledge.module.knowbase.dao.mapper.unstruct.KnowbaseDocumentDirMapper;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentDirService;
/**
 * 知识目录Service业务层处理
 *
 * @author insmess
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KnowbaseDocumentDirServiceImpl  extends ServiceImpl<KnowbaseDocumentDirMapper,KnowbaseDocumentDirPO> implements KnowbaseDocumentDirService {
    @Resource
    private KnowbaseDocumentDirMapper knowbaseDocumentDirMapper;

    @Override
    public Page<KnowbaseDocumentDirPO> pageKnowbaseDocumentDir(KnowbaseDocumentDirPageReqVO pageReqVO) {
        LambdaQueryWrapperX<KnowbaseDocumentDirPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<KnowbaseDocumentDirPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return knowbaseDocumentDirMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveKnowbaseDocumentDir(KnowbaseDocumentDirSaveReqVO saveReqVO) {
        KnowbaseDocumentDirPO knowbaseDocumentDirPO = KnowbaseDocumentDirConvert.INSTANCE.convertToPO(saveReqVO);
        knowbaseDocumentDirMapper.insert(knowbaseDocumentDirPO);
        return knowbaseDocumentDirPO.getId();
    }

    @Override
    public int updateKnowbaseDocumentDir(KnowbaseDocumentDirSaveReqVO updateReqVO) {
        KnowbaseDocumentDirPO updateObj = KnowbaseDocumentDirConvert.INSTANCE.convertToPO(updateReqVO);
        return knowbaseDocumentDirMapper.updateById(updateObj);
    }
    @Override
    public int removeKnowbaseDocumentDir(Collection<Long> idList) {
        return knowbaseDocumentDirMapper.deleteBatchIds(idList);
    }

    @Override
    public KnowbaseDocumentDirPO getKnowbaseDocumentDirById(Long id) {
        return knowbaseDocumentDirMapper.selectById(id);
    }

    @Override
    public List<KnowbaseDocumentDirPO> listKnowbaseDocumentDir() {
        return knowbaseDocumentDirMapper.selectList(null);
    }

    @Override
    public Map<Long, KnowbaseDocumentDirPO> mapKnowbaseDocumentDir() {
        List<KnowbaseDocumentDirPO> knowbaseDocumentDirList = knowbaseDocumentDirMapper.selectList(null);
        return knowbaseDocumentDirList.stream()
                .collect(Collectors.toMap(
                        KnowbaseDocumentDirPO::getId,
                        knowbaseDocumentDirPO -> knowbaseDocumentDirPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入知识目录数据
         *
         * @param importExcelList 知识目录数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKnowbaseDocumentDir(List<KnowbaseDocumentDirRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KnowbaseDocumentDirRespVO respVO : importExcelList) {
                try {
                    KnowbaseDocumentDirPO knowbaseDocumentDirPO = BeanUtils.toBean(respVO, KnowbaseDocumentDirPO.class);
                    Long knowbaseDocumentDirId = respVO.getId();
                    if (isUpdateSupport) {
                        if (knowbaseDocumentDirId != null) {
                            KnowbaseDocumentDirPO existingKnowbaseDocumentDir = knowbaseDocumentDirMapper.selectById(knowbaseDocumentDirId);
                            if (existingKnowbaseDocumentDir != null) {
                                knowbaseDocumentDirMapper.updateById(knowbaseDocumentDirPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + knowbaseDocumentDirId + " 的知识目录记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + knowbaseDocumentDirId + " 的知识目录记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KnowbaseDocumentDirPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", knowbaseDocumentDirId);
                        KnowbaseDocumentDirPO existingKnowbaseDocumentDir = knowbaseDocumentDirMapper.selectOne(queryWrapper);
                        if (existingKnowbaseDocumentDir == null) {
                            knowbaseDocumentDirMapper.insert(knowbaseDocumentDirPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + knowbaseDocumentDirId + " 的知识目录记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + knowbaseDocumentDirId + " 的知识目录记录已存在。");
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

    private LambdaQueryWrapperX<KnowbaseDocumentDirPO> queryCondition(KnowbaseDocumentDirPageReqVO pageReqVO) {
        KnowbaseDocumentDirPO knowbaseDocumentDirPO = KnowbaseDocumentDirConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<KnowbaseDocumentDirPO>()
                .eqIfPresent(KnowbaseDocumentDirPO::getParentId, knowbaseDocumentDirPO.getParentId())
                .likeIfPresent(KnowbaseDocumentDirPO::getName, knowbaseDocumentDirPO.getName())
                .eqIfPresent(KnowbaseDocumentDirPO::getOrderNum, knowbaseDocumentDirPO.getOrderNum())
                .eqIfPresent(KnowbaseDocumentDirPO::getCreateTime, knowbaseDocumentDirPO.getCreateTime())
                .orderByAsc(KnowbaseDocumentDirPO::getCreateTime);
    }

}
