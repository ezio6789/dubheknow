import request from '@/utils/request'

// 查询任务文件关联列表
export function listUnstructTaskDocRel(query) {
  return request({
    url: '/graph/extract/unstructTaskDocRel/list',
    method: 'get',
    params: query
  })
}

// 查询任务文件关联详细
export function getUnstructTaskDocRel(id) {
  return request({
    url: '/graph/extract/unstructTaskDocRel/' + id,
    method: 'get'
  })
}

// 新增任务文件关联
export function addUnstructTaskDocRel(data) {
  return request({
    url: '/graph/extract/unstructTaskDocRel',
    method: 'post',
    data: data
  })
}

// 修改任务文件关联
export function updateUnstructTaskDocRel(data) {
  return request({
    url: '/graph/extract/unstructTaskDocRel',
    method: 'put',
    data: data
  })
}

// 删除任务文件关联
export function delUnstructTaskDocRel(id) {
  return request({
    url: '/graph/extract/unstructTaskDocRel/' + id,
    method: 'delete'
  })
}
