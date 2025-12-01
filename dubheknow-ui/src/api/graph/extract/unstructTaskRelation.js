import request from '@/utils/request'

// 查询任务关系关联列表
export function listUnstructTaskRelation(query) {
  return request({
    url: '/graph/extract/unstructTaskRelation/list',
    method: 'get',
    params: query
  })
}

// 查询任务关系关联详细
export function getUnstructTaskRelation(id) {
  return request({
    url: '/graph/extract/unstructTaskRelation/' + id,
    method: 'get'
  })
}

// 新增任务关系关联
export function addUnstructTaskRelation(data) {
  return request({
    url: '/graph/extract/unstructTaskRelation',
    method: 'post',
    data: data
  })
}

// 修改任务关系关联
export function updateUnstructTaskRelation(data) {
  return request({
    url: '/graph/extract/unstructTaskRelation',
    method: 'put',
    data: data
  })
}

// 删除任务关系关联
export function delUnstructTaskRelation(id) {
  return request({
    url: '/graph/extract/unstructTaskRelation/' + id,
    method: 'delete'
  })
}
