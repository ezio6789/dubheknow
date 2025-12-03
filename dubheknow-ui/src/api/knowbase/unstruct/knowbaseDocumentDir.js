import request from '@/utils/request'

// 查询知识目录列表
export function listKnowbaseDocumentDir(query) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentDir/list',
    method: 'get',
    params: query
  })
}

// 查询知识目录详细
export function getKnowbaseDocumentDir(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentDir/' + id,
    method: 'get'
  })
}

// 新增知识目录
export function addKnowbaseDocumentDir(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentDir',
    method: 'post',
    data: data
  })
}

// 修改知识目录
export function updateKnowbaseDocumentDir(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentDir',
    method: 'put',
    data: data
  })
}

// 删除知识目录
export function delKnowbaseDocumentDir(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocumentDir/' + id,
    method: 'delete'
  })
}
