package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphRelationMappingRespVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphRelationMappingPO;
/**
 * 关系映射Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphRelationMappingService extends IService<GraphRelationMappingPO> {

    /**
     * 获得关系映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系映射分页列表
     */
    Page<GraphRelationMappingPO> pageGraphRelationMapping(GraphRelationMappingPageReqVO pageReqVO);

    /**
     * 创建关系映射
     *
     * @param saveReqVO 关系映射信息
     * @return 关系映射编号
     */
    Long saveGraphRelationMapping(GraphRelationMappingSaveReqVO saveReqVO);

    /**
     * 更新关系映射
     *
     * @param updateReqVO 关系映射信息
     */
    int updateGraphRelationMapping(GraphRelationMappingSaveReqVO updateReqVO);

    /**
     * 删除关系映射
     *
     * @param idList 关系映射编号
     */
    int removeGraphRelationMapping(Collection<Long> idList);

    /**
     * 获得关系映射详情
     *
     * @param id 关系映射编号
     * @return 关系映射
     */
    GraphRelationMappingPO getGraphRelationMappingById(Long id);

    /**
     * 获得全部关系映射列表
     *
     * @return 关系映射列表
     */
    List<GraphRelationMappingPO> listGraphRelationMapping();

    /**
     * 获得全部关系映射 Map
     *
     * @return 关系映射 Map
     */
    Map<Long, GraphRelationMappingPO> mapGraphRelationMapping();


    /**
     * 导入关系映射数据
     *
     * @param importExcelList 关系映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphRelationMapping(List<GraphRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 查询关系映射列表
     *
     * @param relationMappingPageReqVO 关系映射查询参数
     * @return 关系映射列表
     */
    List<GraphRelationMappingPO> list(GraphRelationMappingPageReqVO relationMappingPageReqVO);
}
