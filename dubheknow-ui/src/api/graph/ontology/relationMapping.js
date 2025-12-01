import request from '@/utils/request'

// 查询关系映射列表
export function listRelationMapping(query) {
  return request({
    url: '/graph/ontology/relationMapping/list',
    method: 'get',
    params: query
  })
}

// 查询关系映射详细
export function getRelationMapping(id) {
  return request({
    url: '/graph/ontology/relationMapping/' + id,
    method: 'get'
  })
}

// 新增关系映射
export function addRelationMapping(data) {
  return request({
    url: '/graph/ontology/relationMapping',
    method: 'post',
    data: data
  })
}

// 修改关系映射
export function updateRelationMapping(data) {
  return request({
    url: '/graph/ontology/relationMapping',
    method: 'put',
    data: data
  })
}

// 删除关系映射
export function delRelationMapping(id) {
  return request({
    url: '/graph/ontology/relationMapping/' + id,
    method: 'delete'
  })
}
