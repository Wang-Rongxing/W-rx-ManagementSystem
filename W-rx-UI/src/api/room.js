import request from '../utils/request'

const roomApi = {
  getList: (params) => request({
    url: '/room/allRoom',
    method: 'get',
    params
  }),
  insert: (data) => request({
    url: '/room/insertRoom',
    method: 'post',
    data
  }),
  update: (data) => request({
    url: '/room/updateRoom',
    method: 'post',
    data
  }),
  delete: (id) => request({
    url: `/room/delete/${id}`,
    method: 'delete'
  }),
  search: (data) => request({
    url: '/room/selectRoomByIdOrType',
    method: 'post',
    data
  })
}

export default roomApi
