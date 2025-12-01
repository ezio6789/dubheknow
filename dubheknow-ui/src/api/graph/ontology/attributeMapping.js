import request from '@/utils/request'

// 查询属性映射列表
export function listAttributeMapping(query) {
  return request({
    url: '/graph/ontology/attributeMapping/list',
    method: 'get',
    params: query
  })
}

// 查询属性映射详细
export function getAttributeMapping(id) {
  return request({
    url: '/graph/ontology/attributeMapping/' + id,
    method: 'get'
  })
}

// 新增属性映射
export function addAttributeMapping(data) {
  return request({
    url: '/graph/ontology/attributeMapping',
    method: 'post',
    data: data
  })
}

// 修改属性映射
export function updateAttributeMapping(data) {
  return request({
    url: '/graph/ontology/attributeMapping',
    method: 'put',
    data: data
  })
}

// 删除属性映射
export function delAttributeMapping(id) {
  return request({
    url: '/graph/ontology/attributeMapping/' + id,
    method: 'delete'
  })
}
