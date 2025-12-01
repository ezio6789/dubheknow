import request from '@/utils/request'

// 查询关系配置列表
export function listSchemaRelation(query) {
  return request({
    url: '/graph/ontology/schemaRelation/list',
    method: 'get',
    params: query
  })
}

// 查询关系配置详细
export function getSchemaRelation(id) {
  return request({
    url: '/graph/ontology/schemaRelation/' + id,
    method: 'get'
  })
}

// 新增关系配置
export function addSchemaRelation(data) {
  return request({
    url: '/graph/ontology/schemaRelation',
    method: 'post',
    data: data
  })
}

// 修改关系配置
export function updateSchemaRelation(data) {
  return request({
    url: '/graph/ontology/schemaRelation',
    method: 'put',
    data: data
  })
}

// 删除关系配置
export function delSchemaRelation(id) {
  return request({
    url: '/graph/ontology/schemaRelation/' + id,
    method: 'delete'
  })
}
