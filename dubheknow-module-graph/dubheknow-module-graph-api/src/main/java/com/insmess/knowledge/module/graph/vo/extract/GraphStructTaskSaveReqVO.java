package com.insmess.knowledge.module.graph.vo.extract;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import com.insmess.knowledge.common.core.domain.BaseEntity;

/**
 * 结构化抽取任务 创建/修改 Request VO graph_struct_task
 *
 * @author insmess
 * @date 2025-11-29
 */
@Schema(description = "结构化抽取任务 Response VO")
@Data
public class GraphStructTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long taskId;// 任务id,修改的时候传
    private String taskName;// 任务名称
    private String remark;// 任务备注
    private Long knowledgeId;// 知识图谱id
    private Long datasourceId;// 数据库id
    private List<GraphStructTaskSaveRelationVO.TableData> tableData;// 数据映射表内容

    /**
     * 数据映射表内容
     */
    @Data
    public static class TableData {
        private String tableName;// 表名
        private String tableComment;// 表显示名称
        private String operate;// 对应概念
        private String status;// 状态 0:未映射 1:已映射
        private GraphStructTaskSaveRelationVO.MappingData mappingData;// 映射数据
    }

    /**
     * 映射数据
     */
    @Data
    public static class MappingData {
        private Long dataSourceId;// 数据库id
        private Long concept;// 概念id
        private String conceptName;// 概念名称
        private String tableName;// 表名
        private String entityNameField;// 实体名称字段
        private List<GraphStructTaskSaveRelationVO.Attribute> attributeList;// 属性映射
        private List<GraphStructTaskSaveRelationVO.Relationship> relationshipList;// 关系映射
        private List<GraphStructTaskSaveRelationVO.Custom> customList;// 自定义映射
    }

    /**
     * 属性映射
     */
    @Data
    public static class Attribute {
        private String field;// 列
        private String fieldDescription;// 列描述
        private Long conceptId;// 属性id
        private String conceptName;// 属性名称
    }

    /**
     * 关系映射
     */
    @Data
    public static class Relationship {
        private Long id;// 关系id
        private String field;//列
        private String relation;// 关系
        private String associationTable;// 关联表
        private String associationTableField;// 关联列
        private String associationTableEntityField;// 关联实体名称列
    }

    /**
     * 自定义映射
     */
    @Data
    public static class Custom {
        private String field;// 列
        private String customSQL;// 自定义sql
    }


}
