package com.insmess.knowledge.module.graph.service.manager;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeSaveReqVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgePageReqVO;
import com.insmess.knowledge.module.graph.vo.manager.GraphKnowledgeRespVO;
import com.insmess.knowledge.module.graph.dao.po.manager.GraphKnowledgePO;
/**
 * 知识图谱Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphKnowledgeService extends IService<GraphKnowledgePO> {

    /**
     * 获得知识图谱分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识图谱分页列表
     */
    Page<GraphKnowledgePO> pageGraphKnowledge(GraphKnowledgePageReqVO pageReqVO);

    /**
     * 创建知识图谱
     *
     * @param saveReqVO 知识图谱信息
     * @return 知识图谱编号
     */
    Long saveGraphKnowledge(GraphKnowledgeSaveReqVO saveReqVO);

    /**
     * 更新知识图谱
     *
     * @param updateReqVO 知识图谱信息
     */
    int updateGraphKnowledge(GraphKnowledgeSaveReqVO updateReqVO);

    /**
     * 删除知识图谱
     *
     * @param idList 知识图谱编号
     */
    int removeGraphKnowledge(Collection<Long> idList);

    /**
     * 获得知识图谱详情
     *
     * @param id 知识图谱编号
     * @return 知识图谱
     */
    GraphKnowledgePO getGraphKnowledgeById(Long id);

    /**
     * 获得全部知识图谱列表
     *
     * @return 知识图谱列表
     */
    List<GraphKnowledgePO> listGraphKnowledge();

    /**
     * 获得全部知识图谱 Map
     *
     * @return 知识图谱 Map
     */
    Map<Long, GraphKnowledgePO> mapGraphKnowledge();


    /**
     * 导入知识图谱数据
     *
     * @param importExcelList 知识图谱数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphKnowledge(List<GraphKnowledgeRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
