package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wrx.entity.Role;
import com.wrx.entity.EmployeeRole;
import com.wrx.mapper.EmployeeRoleMapper;
import com.wrx.service.IRoleService;
import com.wrx.service.IEmployeeRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Service
public class EmployeeRoleServiceImpl extends ServiceImpl<EmployeeRoleMapper, EmployeeRole> implements IEmployeeRoleService {

    @Override
    public List<EmployeeRole> selectByUserId(Integer user_Id) {
        LambdaQueryWrapper<EmployeeRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeRole::getEmployeeId, user_Id);
        return this.list(wrapper);
    }

    @Override
    public boolean removeRoleService(Integer user_id) {
        LambdaUpdateWrapper<EmployeeRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EmployeeRole::getEmployeeId, user_id);
        return this.remove(wrapper);
    }
    @Resource
    private IRoleService sysRoleService;
    @Override
    public List<Role> findRoleByUserId(Integer userId) {
        LambdaQueryWrapper<EmployeeRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeRole::getEmployeeId, userId);
        List<EmployeeRole> employeeRoleList = this.list(wrapper);
        //获取 employeeRoleList 中的 role_id 的集合
        if (employeeRoleList != null && employeeRoleList.size() > 0) {
            ArrayList<Integer> roleIdList = new ArrayList<>();
            for (EmployeeRole employeeRole : employeeRoleList) {
                roleIdList.add(employeeRole.getRoleId());
            }
        //根据 roleIdList 查询对应的角色集合
            List<Role> roleList = sysRoleService.listByIds(roleIdList);
            return roleList;
        } else
            return null;
    }
}
