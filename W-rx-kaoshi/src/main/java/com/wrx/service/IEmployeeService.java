package com.wrx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.dto.LoginUserDto;
import com.wrx.dto.EmployeeDto;
import com.wrx.entity.Employee;
import com.wrx.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
public interface IEmployeeService extends IService<Employee> {
    //分页查询user（所有或根据条件）
    Page<Employee> selectByPage(Employee employee, int pageNum, int pageSize);
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    Map<String,Object> selectUserAndRole(Employee employee, int pageIndex, int pageSize);

    boolean updateUserRole(EmployeeDto employeeDto);

    Map<String, Object> selectAllUser(Employee employee, int pageIndex, int pageSize);

    boolean updateUserinitialPerminssion();

    //根据user_id查询role
    List<Role> selectRolesByUserId(Employee employee);

    LoginUserDto login(Employee employee);
}
