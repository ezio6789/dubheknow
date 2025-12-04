package com.insmess.knowledge.module.graph.repository;

import com.insmess.knowledge.module.graph.dao.po.extract.neo4j.GraphExtractionEntity;
import com.insmess.knowledge.neo4j.repository.BaseRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface GraphNeo4jRepository extends BaseRepository<GraphExtractionEntity, Long> {
    /**
     * 根据任务id删除节点
     *
     * @param taskId
     * @return
     */
    @Query("MATCH (a {dynamicProperties_task_id: $taskId}) " +
            "DETACH DELETE a")
    void deleteByTaskId(@Param("taskId") Long taskId);
}
