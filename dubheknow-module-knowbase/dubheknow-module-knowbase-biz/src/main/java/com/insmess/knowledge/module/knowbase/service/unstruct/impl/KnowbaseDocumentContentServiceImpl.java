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
import com.insmess.knowledge.module.knowbase.convert.unstruct.KnowbaseDocumentContentConvert;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentPageReqVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentRespVO;
import com.insmess.knowledge.module.knowbase.vo.unstruct.KnowbaseDocumentContentSaveReqVO;
import com.insmess.knowledge.module.knowbase.dao.po.unstruct.KnowbaseDocumentContentPO;
import com.insmess.knowledge.module.knowbase.dao.mapper.unstruct.KnowbaseDocumentContentMapper;
import com.insmess.knowledge.module.knowbase.service.unstruct.KnowbaseDocumentContentService;
/**
 * 知识内容Service业务层处理
 *
 * @author insmess
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KnowbaseDocumentContentServiceImpl  extends ServiceImpl<KnowbaseDocumentContentMapper,KnowbaseDocumentContentPO> implements KnowbaseDocumentContentService {
    @Resource
    private KnowbaseDocumentContentMapper knowbaseDocumentContentMapper;

    @Override
    public Page<KnowbaseDocumentContentPO> pageKnowbaseDocumentContent(KnowbaseDocumentContentPageReqVO pageReqVO) {
        LambdaQueryWrapperX<KnowbaseDocumentContentPO> wrapper = queryCondition(pageReqVO);
        //设置分页
        Page<KnowbaseDocumentContentPO> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        return knowbaseDocumentContentMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveKnowbaseDocumentContent(KnowbaseDocumentContentSaveReqVO saveReqVO) {
        KnowbaseDocumentContentPO knowbaseDocumentContentPO = KnowbaseDocumentContentConvert.INSTANCE.convertToPO(saveReqVO);
        knowbaseDocumentContentMapper.insert(knowbaseDocumentContentPO);
        return knowbaseDocumentContentPO.getId();
    }

    @Override
    public int updateKnowbaseDocumentContent(KnowbaseDocumentContentSaveReqVO updateReqVO) {
        KnowbaseDocumentContentPO updateObj = KnowbaseDocumentContentConvert.INSTANCE.convertToPO(updateReqVO);
        return knowbaseDocumentContentMapper.updateById(updateObj);
    }
    @Override
    public int removeKnowbaseDocumentContent(Collection<Long> idList) {
        return knowbaseDocumentContentMapper.deleteBatchIds(idList);
    }

    @Override
    public KnowbaseDocumentContentPO getKnowbaseDocumentContentById(Long id) {
        return knowbaseDocumentContentMapper.selectById(id);
    }

    @Override
    public List<KnowbaseDocumentContentPO> listKnowbaseDocumentContent() {
        return knowbaseDocumentContentMapper.selectList(null);
    }

    @Override
    public Map<Long, KnowbaseDocumentContentPO> mapKnowbaseDocumentContent() {
        List<KnowbaseDocumentContentPO> knowbaseDocumentContentList = knowbaseDocumentContentMapper.selectList(null);
        return knowbaseDocumentContentList.stream()
                .collect(Collectors.toMap(
                        KnowbaseDocumentContentPO::getId,
                        knowbaseDocumentContentPO -> knowbaseDocumentContentPO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入知识内容数据
         *
         * @param importExcelList 知识内容数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKnowbaseDocumentContent(List<KnowbaseDocumentContentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KnowbaseDocumentContentRespVO respVO : importExcelList) {
                try {
                    KnowbaseDocumentContentPO knowbaseDocumentContentPO = BeanUtils.toBean(respVO, KnowbaseDocumentContentPO.class);
                    Long knowbaseDocumentContentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (knowbaseDocumentContentId != null) {
                            KnowbaseDocumentContentPO existingKnowbaseDocumentContent = knowbaseDocumentContentMapper.selectById(knowbaseDocumentContentId);
                            if (existingKnowbaseDocumentContent != null) {
                                knowbaseDocumentContentMapper.updateById(knowbaseDocumentContentPO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + knowbaseDocumentContentId + " 的知识内容记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + knowbaseDocumentContentId + " 的知识内容记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KnowbaseDocumentContentPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", knowbaseDocumentContentId);
                        KnowbaseDocumentContentPO existingKnowbaseDocumentContent = knowbaseDocumentContentMapper.selectOne(queryWrapper);
                        if (existingKnowbaseDocumentContent == null) {
                            knowbaseDocumentContentMapper.insert(knowbaseDocumentContentPO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + knowbaseDocumentContentId + " 的知识内容记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + knowbaseDocumentContentId + " 的知识内容记录已存在。");
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

    private LambdaQueryWrapperX<KnowbaseDocumentContentPO> queryCondition(KnowbaseDocumentContentPageReqVO pageReqVO) {
        KnowbaseDocumentContentPO knowbaseDocumentContentPO = KnowbaseDocumentContentConvert.INSTANCE.convertToPO(pageReqVO);
        return new LambdaQueryWrapperX<KnowbaseDocumentContentPO>()
                .eqIfPresent(KnowbaseDocumentContentPO::getDocumentId, knowbaseDocumentContentPO.getDocumentId())
                .eqIfPresent(KnowbaseDocumentContentPO::getContent, knowbaseDocumentContentPO.getContent());
    }

}
