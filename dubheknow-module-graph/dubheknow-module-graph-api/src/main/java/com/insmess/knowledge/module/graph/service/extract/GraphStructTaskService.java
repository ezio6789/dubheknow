package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskPO;
/**
 * 结构化抽取任务Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphStructTaskService extends IService<GraphStructTaskPO> {

    /**
     * 获得结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 结构化抽取任务分页列表
     */
    Page<GraphStructTaskPO> pageGraphStructTask(GraphStructTaskPageReqVO pageReqVO);

    /**
     * 创建结构化抽取任务
     *
     * @param saveReqVO 结构化抽取任务信息
     * @return 结构化抽取任务编号
     */
    Long saveGraphStructTask(GraphStructTaskSaveReqVO saveReqVO);

    /**
     * 更新结构化抽取任务
     *
     * @param updateReqVO 结构化抽取任务信息
     */
    int updateGraphStructTask(GraphStructTaskSaveReqVO updateReqVO);

    /**
     * 删除结构化抽取任务
     *
     * @param idList 结构化抽取任务编号
     */
    int removeGraphStructTask(Collection<Long> idList);

    /**
     * 获得结构化抽取任务详情
     *
     * @param id 结构化抽取任务编号
     * @return 结构化抽取任务
     */
    GraphStructTaskPO getGraphStructTaskById(Long id);

    /**
     * 获得全部结构化抽取任务列表
     *
     * @return 结构化抽取任务列表
     */
    List<GraphStructTaskPO> listGraphStructTask();

    /**
     * 获得全部结构化抽取任务 Map
     *
     * @return 结构化抽取任务 Map
     */
    Map<Long, GraphStructTaskPO> mapGraphStructTask();


    /**
     * 导入结构化抽取任务数据
     *
     * @param importExcelList 结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphStructTask(List<GraphStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 执行结构化抽取任务
     *
     * @param taskId 结构化抽取任务编号
     */
    void executeExtract(Long taskId);
}
