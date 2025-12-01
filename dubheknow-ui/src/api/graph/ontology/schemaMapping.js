import request from '@/utils/request'

// 查询概念映射列表
export function listSchemaMapping(query) {
  return request({
    url: '/graph/ontology/schemaMapping/list',
    method: 'get',
    params: query
  })
}

// 查询概念映射详细
export function getSchemaMapping(id) {
  return request({
    url: '/graph/ontology/schemaMapping/' + id,
    method: 'get'
  })
}

// 新增概念映射
export function addSchemaMapping(data) {
  return request({
    url: '/graph/ontology/schemaMapping',
    method: 'post',
    data: data
  })
}

// 修改概念映射
export function updateSchemaMapping(data) {
  return request({
    url: '/graph/ontology/schemaMapping',
    method: 'put',
    data: data
  })
}

// 删除概念映射
export function delSchemaMapping(id) {
  return request({
    url: '/graph/ontology/schemaMapping/' + id,
    method: 'delete'
  })
}
