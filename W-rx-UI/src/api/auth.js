import request from '../utils/request'

const authApi = {
  login: {
    admin: (data) => request({
      url: '/sysuser/login',
      method: 'post',
      data
    }),
    hotel: (data) => request({
      url: '/employee/login',
      method: 'post',
      data
    }),
    customer: (data) => request({
      url: '/customer/login',
      method: 'post',
      data
    })
  },
  register: (data) => request({
    url: '/customer/register',
    method: 'post',
    data
  })
}

export default authApi
