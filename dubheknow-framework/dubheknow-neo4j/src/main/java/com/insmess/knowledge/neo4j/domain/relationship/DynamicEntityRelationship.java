package com.insmess.knowledge.neo4j.domain.relationship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import com.insmess.knowledge.neo4j.domain.DynamicEntity;

@Data
@RelationshipProperties
public class DynamicEntityRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private DynamicEntity endNode;

    //方向关系，OUT和IN
    private String direction = "OUT";
}
