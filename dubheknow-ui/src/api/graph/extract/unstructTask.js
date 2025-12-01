import request from '@/utils/request'

// 查询非结构化抽取任务列表
export function listUnstructTask(query) {
  return request({
    url: '/graph/extract/unstructTask/list',
    method: 'get',
    params: query
  })
}

// 查询非结构化抽取任务详细
export function getUnstructTask(id) {
  return request({
    url: '/graph/extract/unstructTask/' + id,
    method: 'get'
  })
}

// 新增非结构化抽取任务
export function addUnstructTask(data) {
  return request({
    url: '/graph/extract/unstructTask',
    method: 'post',
    data: data
  })
}

// 修改非结构化抽取任务
export function updateUnstructTask(data) {
  return request({
    url: '/graph/extract/unstructTask',
    method: 'put',
    data: data
  })
}

// 删除非结构化抽取任务
export function delUnstructTask(id) {
  return request({
    url: '/graph/extract/unstructTask/' + id,
    method: 'delete'
  })
}
