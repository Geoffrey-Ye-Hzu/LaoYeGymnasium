// 查询列表数据
const getComboPage = (params) => {
  return $axios({
    url: '/combo/page',
    method: 'get',
    params
  })
}

// 删除数据接口
const deleteCombo = (ids) => {
  return $axios({
    url: '/combo',
    method: 'delete',
    params: { ids }
  })
}

// 修改数据接口
const editCombo = (params) => {
  return $axios({
    url: '/combo',
    method: 'put',
    data: { ...params }
  })
}

// 新增数据接口
const addCombo = (params) => {
  return $axios({
    url: '/combo',
    method: 'post',
    data: { ...params }
  })
}

// 查询详情接口
const queryComboById = (id) => {
  return $axios({
    url: `/combo/${id}`,
    method: 'get'
  })
}

// 批量起售禁售
const setComboByStatus = (params) => {
  return $axios({
    url: `/combo/status/${params.status}`,
    method: 'post',
    params: { ids: params.ids }
  })
}
