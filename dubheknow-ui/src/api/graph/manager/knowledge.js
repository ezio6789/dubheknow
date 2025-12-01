import request from '@/utils/request'

// 查询知识图谱列表
export function listKnowledge(query) {
  return request({
    url: '/graph/manager/knowledge/list',
    method: 'get',
    params: query
  })
}

// 查询知识图谱详细
export function getKnowledge(id) {
  return request({
    url: '/graph/manager/knowledge/' + id,
    method: 'get'
  })
}

// 新增知识图谱
export function addKnowledge(data) {
  return request({
    url: '/graph/manager/knowledge',
    method: 'post',
    data: data
  })
}

// 修改知识图谱
export function updateKnowledge(data) {
  return request({
    url: '/graph/manager/knowledge',
    method: 'put',
    data: data
  })
}

// 删除知识图谱
export function delKnowledge(id) {
  return request({
    url: '/graph/manager/knowledge/' + id,
    method: 'delete'
  })
}
