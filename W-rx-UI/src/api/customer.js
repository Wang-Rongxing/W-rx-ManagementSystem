import request from '../utils/request'

const customerApi = {
  getList: (params) => request({
    url: '/customer/allUser',
    method: 'get',
    params
  }),
  getUserInfo: (params) => request({
    url: '/customer/getUserInfo',
    method: 'get',
    params
  }),
  updatePassword: (data) => request({
    url: '/customer/updatePassword',
    method: 'post',
    data
  }),
  updateUserInfo: (data) => request({
    url: '/customer/updateUserInfo',
    method: 'post',
    data
  }),
  register: (data) => request({
    url: '/customer/register',
    method: 'post',
    data
  }),
  insert: (data) => request({
    url: '/customer/insertUser',
    method: 'post',
    data
  }),
  delete: (id) => request({
    url: `/customer/delete/${id}`,
    method: 'delete'
  }),
  update: (data) => request({
    url: '/customer/updateUser',
    method: 'post',
    data
  }),
  search: (data) => request({
    url: '/customer/selectCustomerByIdOrName',
    method: 'post',
    data
  }),
  getHistory: (params) => request({
    url: '/customer/getHistory',
    method: 'get',
    params
  })
}

export default customerApi
