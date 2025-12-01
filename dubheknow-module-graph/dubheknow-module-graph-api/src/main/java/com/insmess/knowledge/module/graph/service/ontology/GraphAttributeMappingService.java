package com.insmess.knowledge.module.graph.service.ontology;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingSaveReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingPageReqVO;
import com.insmess.knowledge.module.graph.vo.ontology.GraphAttributeMappingRespVO;
import com.insmess.knowledge.module.graph.dao.po.ontology.GraphAttributeMappingPO;
/**
 * 属性映射Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphAttributeMappingService extends IService<GraphAttributeMappingPO> {

    /**
     * 获得属性映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 属性映射分页列表
     */
    Page<GraphAttributeMappingPO> pageGraphAttributeMapping(GraphAttributeMappingPageReqVO pageReqVO);

    /**
     * 创建属性映射
     *
     * @param saveReqVO 属性映射信息
     * @return 属性映射编号
     */
    Long saveGraphAttributeMapping(GraphAttributeMappingSaveReqVO saveReqVO);

    /**
     * 更新属性映射
     *
     * @param updateReqVO 属性映射信息
     */
    int updateGraphAttributeMapping(GraphAttributeMappingSaveReqVO updateReqVO);

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
    GraphAttributeMappingPO getGraphAttributeMappingById(Long id);

    /**
     * 获得全部属性映射列表
     *
     * @return 属性映射列表
     */
    List<GraphAttributeMappingPO> listGraphAttributeMapping();

    /**
     * 获得全部属性映射 Map
     *
     * @return 属性映射 Map
     */
    Map<Long, GraphAttributeMappingPO> mapGraphAttributeMapping();


    /**
     * 导入属性映射数据
     *
     * @param importExcelList 属性映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphAttributeMapping(List<GraphAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
