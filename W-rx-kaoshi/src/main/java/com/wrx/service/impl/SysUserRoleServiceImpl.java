package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wrx.entity.SysRole;
import com.wrx.entity.SysUserRole;
import com.wrx.entity.User;
import com.wrx.mapper.SysUserRoleMapper;
import com.wrx.service.ISysRoleService;
import com.wrx.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public List<SysUserRole> selectByUserId(Long user_Id) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, user_Id);
        return this.list(wrapper);
    }

    @Override
    public boolean removeRoleService(Long user_id) {
        LambdaUpdateWrapper<SysUserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUserRole::getUserId, user_id);
        return this.remove(wrapper);
    }
    @Resource
    private ISysRoleService sysRoleService;
    @Override
    public List<SysRole> findRoleByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>(); wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoleList = this.list(wrapper);
        //获取 sysUserRoleList 中的 role_id 的集合
        if (sysUserRoleList != null && sysUserRoleList.size() > 0) {
        //List<Long> role_id_List = sysUserRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
            ArrayList<Long> role_id_List = new ArrayList<>();//用户角色表中的 user_id 对应的 role_id 的链表
            for (SysUserRole sysUserRole : sysUserRoleList) {
                role_id_List.add(sysUserRole.getRoleId());
            }
        //根据 role_id_List 去角色（sys_role）查询对应的角色的集合
            List<SysRole> sysRoleList = sysRoleService.listByIds(role_id_List);
            return sysRoleList;
        } else
            return null;
    }
}
