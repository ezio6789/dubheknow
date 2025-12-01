package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskDocRelRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskDocRelPO;
/**
 * 任务文件关联Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphUnstructTaskDocRelService extends IService<GraphUnstructTaskDocRelPO> {

    /**
     * 获得任务文件关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务文件关联分页列表
     */
    Page<GraphUnstructTaskDocRelPO> pageGraphUnstructTaskDocRel(GraphUnstructTaskDocRelPageReqVO pageReqVO);

    /**
     * 创建任务文件关联
     *
     * @param saveReqVO 任务文件关联信息
     * @return 任务文件关联编号
     */
    Long saveGraphUnstructTaskDocRel(GraphUnstructTaskDocRelSaveReqVO saveReqVO);

    /**
     * 更新任务文件关联
     *
     * @param updateReqVO 任务文件关联信息
     */
    int updateGraphUnstructTaskDocRel(GraphUnstructTaskDocRelSaveReqVO updateReqVO);

    /**
     * 删除任务文件关联
     *
     * @param idList 任务文件关联编号
     */
    int removeGraphUnstructTaskDocRel(Collection<Long> idList);

    /**
     * 获得任务文件关联详情
     *
     * @param id 任务文件关联编号
     * @return 任务文件关联
     */
    GraphUnstructTaskDocRelPO getGraphUnstructTaskDocRelById(Long id);

    /**
     * 获得全部任务文件关联列表
     *
     * @return 任务文件关联列表
     */
    List<GraphUnstructTaskDocRelPO> listGraphUnstructTaskDocRel();

    /**
     * 获得全部任务文件关联 Map
     *
     * @return 任务文件关联 Map
     */
    Map<Long, GraphUnstructTaskDocRelPO> mapGraphUnstructTaskDocRel();


    /**
     * 导入任务文件关联数据
     *
     * @param importExcelList 任务文件关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphUnstructTaskDocRel(List<GraphUnstructTaskDocRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
