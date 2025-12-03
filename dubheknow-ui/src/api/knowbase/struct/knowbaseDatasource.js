import request from '@/utils/request'

// 查询数据源列表
export function listKnowbaseDatasource(query) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getKnowbaseDatasource(id) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addKnowbaseDatasource(data) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateKnowbaseDatasource(data) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delKnowbaseDatasource(id) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource/' + id,
    method: 'delete'
  })
}

// 测试连接
export function testKnowbaseDatasource(data) {
  return request({
    url: '/knowbase/struct/knowbaseDatasource/test',
    method: 'post',
    data
  })
}
