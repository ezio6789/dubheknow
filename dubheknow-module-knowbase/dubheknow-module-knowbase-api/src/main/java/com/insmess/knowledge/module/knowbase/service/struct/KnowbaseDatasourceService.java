package com.insmess.knowledge.module.knowbase.service.struct;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.insmess.knowledge.database.core.DbColumn;
import com.insmess.knowledge.database.core.DbTable;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceColumnQueryVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceSaveReqVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourcePageReqVO;
import com.insmess.knowledge.module.knowbase.vo.struct.KnowbaseDatasourceRespVO;
import com.insmess.knowledge.module.knowbase.dao.po.struct.KnowbaseDatasourcePO;
/**
 * 数据源Service接口
 *
 * @author insmess
 * @date 2025-12-04
 */
public interface KnowbaseDatasourceService extends IService<KnowbaseDatasourcePO> {

    /**
     * 获得数据源分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据源分页列表
     */
    Page<KnowbaseDatasourcePO> pageKnowbaseDatasource(KnowbaseDatasourcePageReqVO pageReqVO);

    /**
     * 创建数据源
     *
     * @param saveReqVO 数据源信息
     * @return 数据源编号
     */
    Long saveKnowbaseDatasource(KnowbaseDatasourceSaveReqVO saveReqVO);

    /**
     * 更新数据源
     *
     * @param updateReqVO 数据源信息
     */
    int updateKnowbaseDatasource(KnowbaseDatasourceSaveReqVO updateReqVO);

    /**
     * 删除数据源
     *
     * @param idList 数据源编号
     */
    int removeKnowbaseDatasource(Collection<Long> idList);

    /**
     * 获得数据源详情
     *
     * @param id 数据源编号
     * @return 数据源
     */
    KnowbaseDatasourcePO getKnowbaseDatasourceById(Long id);

    /**
     * 获得全部数据源列表
     *
     * @return 数据源列表
     */
    List<KnowbaseDatasourcePO> listKnowbaseDatasource();

    /**
     * 获得全部数据源 Map
     *
     * @return 数据源 Map
     */
    Map<Long, KnowbaseDatasourcePO> mapKnowbaseDatasource();


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKnowbaseDatasource(List<KnowbaseDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 测试数据源连接
     *
     * @param id 数据源编号
     */
    void testConnection(Long id);

    /**
     * 获得数据源表列表
     *
     * @param id 数据源编号
     * @return 数据源表列表
     */
    List<DbTable> listTable(Long id);

    /**
     * 获取列
     * @param queryVO
     * @return
     */
    List<DbColumn> listColumns(KnowbaseDatasourceColumnQueryVO queryVO);
}
