package com.wrx.service;

import com.wrx.entity.Role;
import com.wrx.entity.EmployeeRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
public interface IEmployeeRoleService extends IService<EmployeeRole> {
    //根据user_id查询SysUserRole
    List<EmployeeRole> selectByUserId(Integer user_Id);

    boolean removeRoleService(Integer user_id);
    //根据 userId 查询 user 的角色信息
    List<Role> findRoleByUserId(Integer userId);
}