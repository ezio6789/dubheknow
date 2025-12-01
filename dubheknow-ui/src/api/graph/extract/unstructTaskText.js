import request from '@/utils/request'

// 查询任务文件段落关联列表
export function listUnstructTaskText(query) {
  return request({
    url: '/graph/extract/unstructTaskText/list',
    method: 'get',
    params: query
  })
}

// 查询任务文件段落关联详细
export function getUnstructTaskText(id) {
  return request({
    url: '/graph/extract/unstructTaskText/' + id,
    method: 'get'
  })
}

// 新增任务文件段落关联
export function addUnstructTaskText(data) {
  return request({
    url: '/graph/extract/unstructTaskText',
    method: 'post',
    data: data
  })
}

// 修改任务文件段落关联
export function updateUnstructTaskText(data) {
  return request({
    url: '/graph/extract/unstructTaskText',
    method: 'put',
    data: data
  })
}

// 删除任务文件段落关联
export function delUnstructTaskText(id) {
  return request({
    url: '/graph/extract/unstructTaskText/' + id,
    method: 'delete'
  })
}
