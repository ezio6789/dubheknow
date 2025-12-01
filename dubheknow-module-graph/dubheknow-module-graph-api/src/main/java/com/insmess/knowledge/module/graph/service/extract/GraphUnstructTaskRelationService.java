package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRelationRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskRelationPO;
/**
 * 任务关系关联Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphUnstructTaskRelationService extends IService<GraphUnstructTaskRelationPO> {

    /**
     * 获得任务关系关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务关系关联分页列表
     */
    Page<GraphUnstructTaskRelationPO> pageGraphUnstructTaskRelation(GraphUnstructTaskRelationPageReqVO pageReqVO);

    /**
     * 创建任务关系关联
     *
     * @param saveReqVO 任务关系关联信息
     * @return 任务关系关联编号
     */
    Long saveGraphUnstructTaskRelation(GraphUnstructTaskRelationSaveReqVO saveReqVO);

    /**
     * 更新任务关系关联
     *
     * @param updateReqVO 任务关系关联信息
     */
    int updateGraphUnstructTaskRelation(GraphUnstructTaskRelationSaveReqVO updateReqVO);

    /**
     * 删除任务关系关联
     *
     * @param idList 任务关系关联编号
     */
    int removeGraphUnstructTaskRelation(Collection<Long> idList);

    /**
     * 获得任务关系关联详情
     *
     * @param id 任务关系关联编号
     * @return 任务关系关联
     */
    GraphUnstructTaskRelationPO getGraphUnstructTaskRelationById(Long id);

    /**
     * 获得全部任务关系关联列表
     *
     * @return 任务关系关联列表
     */
    List<GraphUnstructTaskRelationPO> listGraphUnstructTaskRelation();

    /**
     * 获得全部任务关系关联 Map
     *
     * @return 任务关系关联 Map
     */
    Map<Long, GraphUnstructTaskRelationPO> mapGraphUnstructTaskRelation();


    /**
     * 导入任务关系关联数据
     *
     * @param importExcelList 任务关系关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphUnstructTaskRelation(List<GraphUnstructTaskRelationRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
