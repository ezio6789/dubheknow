# 环境

| 组件  | 版本  | 说明       |
| ----- | ----- | ---------- |
| neo4j | 4.4.40 | 图数据库   |
| mysql | 8     | 关系数据库 |
| redis |       |            |

# 部署

安装neo4j

```shell
docker run -idt --restart always -v /data/neo4j:/data 7474:7474 --publish=7687:7687 -env=NEO4J_AUTH=neo4j neo4j:4.4.40 
```

