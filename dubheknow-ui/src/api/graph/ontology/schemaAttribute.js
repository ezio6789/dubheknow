import request from '@/utils/request'

// 查询概念属性列表
export function listSchemaAttribute(query) {
  return request({
    url: '/graph/ontology/schemaAttribute/list',
    method: 'get',
    params: query
  })
}

// 查询概念属性详细
export function getSchemaAttribute(id) {
  return request({
    url: '/graph/ontology/schemaAttribute/' + id,
    method: 'get'
  })
}

// 新增概念属性
export function addSchemaAttribute(data) {
  return request({
    url: '/graph/ontology/schemaAttribute',
    method: 'post',
    data: data
  })
}

// 修改概念属性
export function updateSchemaAttribute(data) {
  return request({
    url: '/graph/ontology/schemaAttribute',
    method: 'put',
    data: data
  })
}

// 删除概念属性
export function delSchemaAttribute(id) {
  return request({
    url: '/graph/ontology/schemaAttribute/' + id,
    method: 'delete'
  })
}
