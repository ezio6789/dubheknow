package com.insmess.knowledge.module.graph.service.extract;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextSaveReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextPageReqVO;
import com.insmess.knowledge.module.graph.vo.extract.GraphUnstructTaskTextRespVO;
import com.insmess.knowledge.module.graph.dao.po.extract.GraphUnstructTaskTextPO;
/**
 * 任务文件段落关联Service接口
 *
 * @author insmess
 * @date 2025-11-29
 */
public interface GraphUnstructTaskTextService extends IService<GraphUnstructTaskTextPO> {

    /**
     * 获得任务文件段落关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务文件段落关联分页列表
     */
    Page<GraphUnstructTaskTextPO> pageGraphUnstructTaskText(GraphUnstructTaskTextPageReqVO pageReqVO);

    /**
     * 创建任务文件段落关联
     *
     * @param saveReqVO 任务文件段落关联信息
     * @return 任务文件段落关联编号
     */
    Long saveGraphUnstructTaskText(GraphUnstructTaskTextSaveReqVO saveReqVO);

    /**
     * 更新任务文件段落关联
     *
     * @param updateReqVO 任务文件段落关联信息
     */
    int updateGraphUnstructTaskText(GraphUnstructTaskTextSaveReqVO updateReqVO);

    /**
     * 删除任务文件段落关联
     *
     * @param idList 任务文件段落关联编号
     */
    int removeGraphUnstructTaskText(Collection<Long> idList);

    /**
     * 获得任务文件段落关联详情
     *
     * @param id 任务文件段落关联编号
     * @return 任务文件段落关联
     */
    GraphUnstructTaskTextPO getGraphUnstructTaskTextById(Long id);

    /**
     * 获得全部任务文件段落关联列表
     *
     * @return 任务文件段落关联列表
     */
    List<GraphUnstructTaskTextPO> listGraphUnstructTaskText();

    /**
     * 获得全部任务文件段落关联 Map
     *
     * @return 任务文件段落关联 Map
     */
    Map<Long, GraphUnstructTaskTextPO> mapGraphUnstructTaskText();


    /**
     * 导入任务文件段落关联数据
     *
     * @param importExcelList 任务文件段落关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importGraphUnstructTaskText(List<GraphUnstructTaskTextRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
