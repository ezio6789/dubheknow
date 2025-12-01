package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskPO;
/**
 * 非结构化抽取任务Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphUnstructTaskService extends IService<GraphUnstructTaskPO> {

    /**
     * 获得非结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 非结构化抽取任务分页列表
     */
    Page<GraphUnstructTaskPO> pageGraphUnstructTask(GraphUnstructTaskPageReqVO pageReqVO);

    /**
     * 创建非结构化抽取任务
     *
     * @param saveReqVO 非结构化抽取任务信息
     * @return 非结构化抽取任务编号
     */
    Long saveGraphUnstructTask(GraphUnstructTaskSaveReqVO saveReqVO);

    /**
     * 更新非结构化抽取任务
     *
     * @param updateReqVO 非结构化抽取任务信息
     */
    int updateGraphUnstructTask(GraphUnstructTaskSaveReqVO updateReqVO);

    /**
     * 删除非结构化抽取任务
     *
     * @param idList 非结构化抽取任务编号
     */
    int removeGraphUnstructTask(Collection<Long> idList);

    /**
     * 获得非结构化抽取任务详情
     *
     * @param id 非结构化抽取任务编号
     * @return 非结构化抽取任务
     */
    GraphUnstructTaskPO getGraphUnstructTaskById(Long id);

    /**
     * 获得全部非结构化抽取任务列表
     *
     * @return 非结构化抽取任务列表
     */
    List<GraphUnstructTaskPO> listGraphUnstructTask();

    /**
     * 获得全部非结构化抽取任务 Map
     *
     * @return 非结构化抽取任务 Map
     */
    Map<Long, GraphUnstructTaskPO> mapGraphUnstructTask();


    /**
     * 导入非结构化抽取任务数据
     *
     * @param importExcelList 非结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphUnstructTask(List<GraphUnstructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
