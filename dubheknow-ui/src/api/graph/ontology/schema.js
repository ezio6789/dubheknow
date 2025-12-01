import request from '@/utils/request'

// 查询概念配置列表
export function listSchema(query) {
  return request({
    url: '/graph/ontology/schema/list',
    method: 'get',
    params: query
  })
}

// 查询概念配置详细
export function getSchema(id) {
  return request({
    url: '/graph/ontology/schema/' + id,
    method: 'get'
  })
}

// 新增概念配置
export function addSchema(data) {
  return request({
    url: '/graph/ontology/schema',
    method: 'post',
    data: data
  })
}

// 修改概念配置
export function updateSchema(data) {
  return request({
    url: '/graph/ontology/schema',
    method: 'put',
    data: data
  })
}

// 删除概念配置
export function delSchema(id) {
  return request({
    url: '/graph/ontology/schema/' + id,
    method: 'delete'
  })
}


// 获取概念配置树
export function getExtSchemaTree(data) {
  return request({
    url: '/graph/ontology/schema/tree',
    method: 'get',
    params:data
  })
}

// 查询概念图
export function getExtSchemaGraph(data) {
    return request({
        url: "/graph/ontology/schema/getGraph",
        method: "get",
        params:data
    })
}

// 根据节点id删除对应的节点
export function deleteNode(id) {
  return request({
    url: `/app/graph/deleteNode/${id}`,
    method: "delete",
  });
}