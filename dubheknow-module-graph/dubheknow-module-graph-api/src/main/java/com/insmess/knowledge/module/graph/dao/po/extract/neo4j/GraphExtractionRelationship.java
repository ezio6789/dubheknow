package com.insmess.knowledge.module.graph.dao.po.extract.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class GraphExtractionRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private GraphExtractionEntity endNode;
}
