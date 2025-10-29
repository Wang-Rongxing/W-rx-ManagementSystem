package com.wrx.service;

import com.wrx.entity.SysRole;
import com.wrx.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.entity.User;

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
public interface ISysUserRoleService extends IService<SysUserRole> {
    //根据user_id查询SysUserRole
    List<SysUserRole> selectByUserId(Long user_Id);

    boolean removeRoleService(Long user_id);
    //根据 userId 查询 user 的角色信息
    List<SysRole> findRoleByUserId(Long userId);
}