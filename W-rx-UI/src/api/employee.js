import request from '../utils/request'

const employeeApi = {
  getList: (params) => request({
    url: '/employee/allUser',
    method: 'get',
    params
  }),
  getWithRoles: (params) => request({
    url: '/employee/userWithRoleByPage',
    method: 'get',
    params
  }),
  insert: (data) => request({
    url: '/employee/insertUser',
    method: 'post',
    data
  }),
  updateRole: (data) => request({
    url: '/employee/updateUserRole',
    method: 'post',
    data
  }),
  delete: (id) => request({
    url: `/employee/delete/${id}`,
    method: 'delete'
  }),
  resetPassword: (params) => request({
    url: '/employee/resetUserPassword',
    method: 'get',
    params
  }),
  search: (data) => request({
    url: '/employee/selectEmployeeByIdOrName',
    method: 'post',
    data
  })
}

export default employeeApi
