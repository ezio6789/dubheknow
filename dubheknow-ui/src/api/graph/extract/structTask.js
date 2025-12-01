import request from '@/utils/request'

// 查询结构化抽取任务列表
export function listStructTask(query) {
  return request({
    url: '/graph/extract/structTask/list',
    method: 'get',
    params: query
  })
}

// 查询结构化抽取任务详细
export function getStructTask(id) {
  return request({
    url: '/graph/extract/structTask/' + id,
    method: 'get'
  })
}

// 新增结构化抽取任务
export function addStructTask(data) {
  return request({
    url: '/graph/extract/structTask',
    method: 'post',
    data: data
  })
}

// 修改结构化抽取任务
export function updateStructTask(data) {
  return request({
    url: '/graph/extract/structTask',
    method: 'put',
    data: data
  })
}

// 删除结构化抽取任务
export function delStructTask(id) {
  return request({
    url: '/graph/extract/structTask/' + id,
    method: 'delete'
  })
}
