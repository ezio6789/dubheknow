import request from '@/utils/request'

// 查询知识文件列表
export function listKnowbaseDocument(query) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocument/list',
    method: 'get',
    params: query
  })
}

// 查询知识文件详细
export function getKnowbaseDocument(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocument/' + id,
    method: 'get'
  })
}

// 新增知识文件
export function addKnowbaseDocument(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocument',
    method: 'post',
    data: data
  })
}

// 修改知识文件
export function updateKnowbaseDocument(data) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocument',
    method: 'put',
    data: data
  })
}

// 删除知识文件
export function delKnowbaseDocument(id) {
  return request({
    url: '/knowbase/unstruct/knowbaseDocument/' + id,
    method: 'delete'
  })
}
