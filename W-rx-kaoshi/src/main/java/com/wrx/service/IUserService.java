package com.wrx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.dto.LoginUserDto;
import com.wrx.dto.UserDto;
import com.wrx.entity.SysRole;
import com.wrx.entity.User;
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
public interface IUserService extends IService<User> {
    //分页查询user（所有或根据条件）
    Page<User> selectByPage(User user,int pageNum,int pageSize);
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    Map<String,Object> selectUserAndRole(User user,int pageIndex,int pageSize);

    boolean updateUserRole(UserDto userDto);

    Map<String, Object> selectAllUser(User user, int pageIndex, int pageSize);

    boolean updateUserinitialPerminssion();

    //根据user_id查询role
    List<SysRole> selectRolesByUserId(User user);

    LoginUserDto login(User user);
}
