package com.insmess.knowledge.module.graph.dao.po.extract.neo4j;

import lombok.Data;

public class GraphExtractionMergeDO {

    @Data
    public static class Node{
        private String name;
        private Long task_id;
        private String table_name;
        private Long data_id;
        private Integer entity_type;
        private Long database_id;
    }

    @Data
    public static class CreateRelationshipNode{
        private Long data_id;
        private Long task_id;
        private String table_name;
    }
}
