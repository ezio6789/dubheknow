package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskRelationMappingRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskRelationMappingPO;

/**
 * 关系映射Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphStructTaskRelationMappingService extends IService<GraphStructTaskRelationMappingPO> {

    /**
     * 获得关系映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系映射分页列表
     */
    Page<GraphStructTaskRelationMappingPO> pageGraphRelationMapping(GraphStructTaskRelationMappingPageReqVO pageReqVO);

    /**
     * 创建关系映射
     *
     * @param saveReqVO 关系映射信息
     * @return 关系映射编号
     */
    Long saveGraphRelationMapping(GraphStructTaskRelationMappingSaveReqVO saveReqVO);

    /**
     * 更新关系映射
     *
     * @param updateReqVO 关系映射信息
     */
    int updateGraphRelationMapping(GraphStructTaskRelationMappingSaveReqVO updateReqVO);

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
    GraphStructTaskRelationMappingPO getGraphRelationMappingById(Long id);

    /**
     * 获得全部关系映射列表
     *
     * @return 关系映射列表
     */
    List<GraphStructTaskRelationMappingPO> listGraphRelationMapping();

    /**
     * 获得全部关系映射 Map
     *
     * @return 关系映射 Map
     */
    Map<Long, GraphStructTaskRelationMappingPO> mapGraphRelationMapping();


    /**
     * 导入关系映射数据
     *
     * @param importExcelList 关系映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphRelationMapping(List<GraphStructTaskRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 查询关系映射列表
     *
     * @param relationMappingPageReqVO 关系映射查询参数
     * @return 关系映射列表
     */
    List<GraphStructTaskRelationMappingPO> list(GraphStructTaskRelationMappingPageReqVO relationMappingPageReqVO);

    /**
     * 根据任务ID查询关系映射列表
     *
     * @param id 任务ID
     * @return 关系映射列表
     */
    List<GraphStructTaskRelationMappingPO> listByTaskId(Long id);
}
