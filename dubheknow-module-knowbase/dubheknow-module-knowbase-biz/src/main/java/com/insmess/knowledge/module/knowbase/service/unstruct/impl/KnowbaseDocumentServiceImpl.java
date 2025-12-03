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
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentConvert;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentPO;
import com.insmess.knowledge.module.knowbase.dao.mapper.unstruct.KnowbaseDocumentMapper;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentService;
/**
 * 知识文件Service业务层处理
 *
 * @author insmess
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KnowbaseDocumentServiceImpl  extends ServiceImpl<KnowbaseDocumentMapper,KnowbaseDocumentPO> implements KnowbaseDocumentService {
    @Resource
    private KnowbaseDocumentMapper knowbaseDocumentMapper;

    @Override
    public Page<KnowbaseDocumentPO> pageKnowbaseDocument(KnowbaseDocumentPageReqVO pageReqVO) {
        LambdaQueryWrapperX<KnowbaseDocumentPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<KnowbaseDocumentPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return knowbaseDocumentMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveKnowbaseDocument(KnowbaseDocumentSaveReqVO saveReqVO) {
        KnowbaseDocumentPO knowbaseDocumentPO = KnowbaseDocumentConvert.INSTANCE.convertToPO(saveReqVO);
        knowbaseDocumentMapper.insert(knowbaseDocumentPO);
        return knowbaseDocumentPO.getId();
    }

    @Override
    public int updateKnowbaseDocument(KnowbaseDocumentSaveReqVO updateReqVO) {
        KnowbaseDocumentPO updateObj = KnowbaseDocumentConvert.INSTANCE.convertToPO(updateReqVO);
        return knowbaseDocumentMapper.updateById(updateObj);
    }
    @Override
    public int removeKnowbaseDocument(Collection<Long> idList) {
        return knowbaseDocumentMapper.deleteBatchIds(idList);
    }

    @Override
    public KnowbaseDocumentPO getKnowbaseDocumentById(Long id) {
        return knowbaseDocumentMapper.selectById(id);
    }

    @Override
    public List<KnowbaseDocumentPO> listKnowbaseDocument() {
        return knowbaseDocumentMapper.selectList(null);
    }

    @Override
    public Map<Long, KnowbaseDocumentPO> mapKnowbaseDocument() {
        List<KnowbaseDocumentPO> knowbaseDocumentList = knowbaseDocumentMapper.selectList(null);
        return knowbaseDocumentList.stream()
                .collect(Collectors.toMap(
                        KnowbaseDocumentPO::getId,
                        knowbaseDocumentPO -> knowbaseDocumentPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入知识文件数据
         *
         * @param importExcelList 知识文件数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKnowbaseDocument(List<KnowbaseDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KnowbaseDocumentRespVO respVO : importExcelList) {
                try {
                    KnowbaseDocumentPO knowbaseDocumentPO = BeanUtils.toBean(respVO, KnowbaseDocumentPO.class);
                    Long knowbaseDocumentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (knowbaseDocumentId != null) {
                            KnowbaseDocumentPO existingKnowbaseDocument = knowbaseDocumentMapper.selectById(knowbaseDocumentId);
                            if (existingKnowbaseDocument != null) {
                                knowbaseDocumentMapper.updateById(knowbaseDocumentPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + knowbaseDocumentId + " 的知识文件记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + knowbaseDocumentId + " 的知识文件记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KnowbaseDocumentPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", knowbaseDocumentId);
                        KnowbaseDocumentPO existingKnowbaseDocument = knowbaseDocumentMapper.selectOne(queryWrapper);
                        if (existingKnowbaseDocument == null) {
                            knowbaseDocumentMapper.insert(knowbaseDocumentPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + knowbaseDocumentId + " 的知识文件记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + knowbaseDocumentId + " 的知识文件记录已存在。");
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

    private LambdaQueryWrapperX<KnowbaseDocumentPO> queryCondition(KnowbaseDocumentPageReqVO pageReqVO) {
        KnowbaseDocumentPO knowbaseDocumentPO = KnowbaseDocumentConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<KnowbaseDocumentPO>()
                .eqIfPresent(KnowbaseDocumentPO::getCategoryId, knowbaseDocumentPO.getCategoryId())
                .likeIfPresent(KnowbaseDocumentPO::getCategoryName, knowbaseDocumentPO.getCategoryName())
                .likeIfPresent(KnowbaseDocumentPO::getName, knowbaseDocumentPO.getName())
                .eqIfPresent(KnowbaseDocumentPO::getType, knowbaseDocumentPO.getType())
                .eqIfPresent(KnowbaseDocumentPO::getParseStatus, knowbaseDocumentPO.getParseStatus())
                .eqIfPresent(KnowbaseDocumentPO::getUrl, knowbaseDocumentPO.getUrl())
                .eqIfPresent(KnowbaseDocumentPO::getDescription, knowbaseDocumentPO.getDescription())
                .eqIfPresent(KnowbaseDocumentPO::getCreateTime, knowbaseDocumentPO.getCreateTime())
                .orderByAsc(KnowbaseDocumentPO::getCreateTime);
    }

}
