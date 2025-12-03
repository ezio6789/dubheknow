import request from '@/utils/request'

// 查询知识内容列表
export function listKnowbaseDocumentContent(query) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentContent/list',
    method: 'get',
    params: query
  })
}

// 查询知识内容详细
export function getKnowbaseDocumentContent(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentContent/' + id,
    method: 'get'
  })
}

// 新增知识内容
export function addKnowbaseDocumentContent(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentContent',
    method: 'post',
    data: data
  })
}

// 修改知识内容
export function updateKnowbaseDocumentContent(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentContent',
    method: 'put',
    data: data
  })
}

// 删除知识内容
export function delKnowbaseDocumentContent(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentContent/' + id,
    method: 'delete'
  })
}
