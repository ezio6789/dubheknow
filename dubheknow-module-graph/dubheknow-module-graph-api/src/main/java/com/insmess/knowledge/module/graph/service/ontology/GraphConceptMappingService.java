package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphConceptMappingPO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphConceptMappingRespVO;

/**
 * 概念映射Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphConceptMappingService extends IService<GraphConceptMappingPO> {

    /**
     * 根据任务编号获得概念映射列表
     *
     * @param taskId 任务编号
     * @return 概念映射列表
     */
    List<GraphConceptMappingPO> listByTaskId(Long taskId);

    /**
     * 获得概念映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念映射分页列表
     */
    Page<GraphConceptMappingPO> pageGraphConceptMapping(GraphConceptMappingPageReqVO pageReqVO);

    /**
     * 创建概念映射
     *
     * @param saveReqVO 概念映射信息
     * @return 概念映射编号
     */
    Long saveGraphConceptMapping(GraphConceptMappingSaveReqVO saveReqVO);

    /**
     * 更新概念映射
     *
     * @param updateReqVO 概念映射信息
     */
    int updateGraphConceptMapping(GraphConceptMappingSaveReqVO updateReqVO);

    /**
     * 删除概念映射
     *
     * @param idList 概念映射编号
     */
    int removeGraphConceptMapping(Collection<Long> idList);

    /**
     * 获得概念映射详情
     *
     * @param id 概念映射编号
     * @return 概念映射
     */
    GraphConceptMappingPO getGraphConceptMappingById(Long id);

    /**
     * 获得全部概念映射列表
     *
     * @return 概念映射列表
     */
    List<GraphConceptMappingPO> listGraphConceptMapping();

    /**
     * 获得全部概念映射 Map
     *
     * @return 概念映射 Map
     */
    Map<Long, GraphConceptMappingPO> mapGraphConceptMapping();


    /**
     * 导入概念映射数据
     *
     * @param importExcelList 概念映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphConceptMapping(List<GraphConceptMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
