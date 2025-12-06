package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphStructTaskAttributeMappingPO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphStructTaskAttributeMappingRespVO;

/**
 * 属性映射Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphStructTaskAttributeMappingService extends IService<GraphStructTaskAttributeMappingPO> {

    /**
     * 获得属性映射列表
     *
     * @return 属性映射列表
     */
    List<GraphStructTaskAttributeMappingPO> list(GraphStructTaskAttributeMappingPageReqVO pageReqVO);

    /**
     * 获得属性映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 属性映射分页列表
     */
    Page<GraphStructTaskAttributeMappingPO> pageGraphAttributeMapping(GraphStructTaskAttributeMappingPageReqVO pageReqVO);

    /**
     * 创建属性映射
     *
     * @param saveReqVO 属性映射信息
     * @return 属性映射编号
     */
    Long saveGraphAttributeMapping(GraphStructTaskAttributeMappingSaveReqVO saveReqVO);

    /**
     * 更新属性映射
     *
     * @param updateReqVO 属性映射信息
     */
    int updateGraphAttributeMapping(GraphStructTaskAttributeMappingSaveReqVO updateReqVO);

    /**
     * 删除属性映射
     *
     * @param idList 属性映射编号
     */
    int removeGraphAttributeMapping(Collection<Long> idList);

    /**
     * 获得属性映射详情
     *
     * @param id 属性映射编号
     * @return 属性映射
     */
    GraphStructTaskAttributeMappingPO getGraphAttributeMappingById(Long id);

    /**
     * 获得全部属性映射列表
     *
     * @return 属性映射列表
     */
    List<GraphStructTaskAttributeMappingPO> listGraphAttributeMapping();

    /**
     * 获得全部属性映射 Map
     *
     * @return 属性映射 Map
     */
    Map<Long, GraphStructTaskAttributeMappingPO> mapGraphAttributeMapping();


    /**
     * 导入属性映射数据
     *
     * @param importExcelList 属性映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphAttributeMapping(List<GraphStructTaskAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据任务ID查询属性映射
     * @param id
     * @return
     */
    List<GraphStructTaskAttributeMappingPO> listByTaskId(Long id);
}
