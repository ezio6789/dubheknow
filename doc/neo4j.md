根据id跳阶查询

```neo4j
MATCH (n)
WHERE id(n) = 1067
CALL apoc.path.subgraphAll(n, {
  maxLevel: 3,
  relationshipFilter: '>|<',   // ✅ 明确指定可走出边和入边
  labelFilter: '',
  bfs: true,
  limit: 1000
})
YIELD nodes, relationships
RETURN nodes, relationships;

```

分页、条件、跳阶查询

```
MATCH (n)
WHERE n.dynamicProperties_release_status = 0
AND n.dynamicProperties_task_id = 6
WITH n
ORDER BY id(n)
SKIP 0 LIMIT 10
CALL apoc.path.subgraphAll(n, {
  maxLevel: 3,
  relationshipFilter: '>|<',   // ✅ 明确指定可走出边和入边
  labelFilter: '',
  bfs: true,
  limit: 20
})
YIELD nodes, relationships
RETURN nodes, relationships
```

测试

```
 MATCH (n) WHERE id(n) = 1067 CALL apoc.path.subgraphAll(n, { maxLevel: 5, relationshipFilter: '>|<', labelFilter: '', bfs: true, limit: 100 }) YIELD nodes, relationships RETURN nodes, relationships;
```

```
MATCH (n)
            WHERE n.dynamicProperties_release_status = 0
              AND n.dynamicProperties_task_id = 9
            WITH n
            ORDER BY id(n)
            SKIP 0 LIMIT 10

            CALL apoc.path.subgraphAll(n, {
              relationshipFilter: '<>',
              bfs: false,
              maxLevel: 4,
              limit: 10000
            })
            YIELD relationships

            UNWIND relationships AS r
            RETURN 
              id(startNode(r)) AS startId,
              startNode(r).name AS startName,
              type(r) AS relType,
              id(endNode(r)) AS endId,
              endNode(r).name AS endName
            ORDER BY startId
```

