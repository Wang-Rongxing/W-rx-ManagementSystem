package com.wrx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.dto.LoginUserDto;
import com.wrx.dto.UserDto;
import com.wrx.entity.SysRole;
import com.wrx.entity.SysUserRole;
import com.wrx.entity.User;
import com.wrx.mapper.UserMapper;
import com.wrx.service.ISysRoleService;
import com.wrx.service.ISysUserRoleService;
import com.wrx.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginUser;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {



    //分页查询user（所有或根据条件）
    @Override
    public Page<User> selectByPage(User user,int pageIndex,int pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        if (StringUtils.isNotBlank(user.getJobId()))
            wrapper.eq(User::getJobId, user.getJobId());
        if (StringUtils.isNotBlank(user.getUsername()))
            wrapper.eq(User::getUsername, user.getUsername());
        Page<User> userPage = new Page<>(pageIndex,pageSize);
        return this.page(userPage, wrapper);
    }
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private ISysRoleService sysRoleService;
    @Override
    public Map<String, Object> selectUserAndRole(User user,int pageIndex,int pageSize) {
        //分页查询user（所有或根据条件）
        Page<User> userPage = this.selectByPage(user,pageIndex,pageSize);
        List<User> userList = userPage.getRecords();
        //遍历userList，并查询每个user的角色，重新封装成UserDto的链表
        ArrayList<UserDto> userDtoList = new ArrayList<>();
        if(!userList.isEmpty()){
            for (User u : userList) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(u,userDto);
                //根据user_id查询SysUserRole
                List<SysRole> sysRoleList = this.selectRolesByUserId(u);
                //把角色链表存入userDto
                userDto.setSysRoleList(sysRoleList);
                //把每一个生成的userDto存入链表
                userDtoList.add(userDto);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",userDtoList);
        map.put("total",userPage.getTotal());
        map.put("pages",userPage.getPages());
        return map;
    }
    //更新权限
    @Transactional //事务注解
    @Override
    public boolean updateUserRole(UserDto userDto) {
        //根据user_id删除user对应的权限
        boolean b1 = sysUserRoleService.removeRoleService(userDto.getId());
        //新增选择的权限
        ArrayList<SysUserRole> sysUserRoleList = new ArrayList<>();
        for(SysRole sysRole : userDto.getSysRoleList()){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(sysRole.getId());
            sysUserRole.setUserId(userDto.getId());
            sysUserRoleList.add(sysUserRole);
        }
        //批量把sysUserRoleList添加
        boolean b2 = sysUserRoleService.saveBatch(sysUserRoleList);
        return b1 || b2;
    }
    //教师管理
    @Override
    public Map<String, Object> selectAllUser(User user, int pageIndex, int pageSize) {
        Page<User> userPage = this.selectByPage(user,pageIndex,pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",userPage.getRecords());
        map.put("total",userPage.getTotal());
        map.put("pages",userPage.getPages());
        return map;
    }
    //TODO 权限管理-批量授权
    @Override
    public boolean updateUserinitialPerminssion() {
        LambdaQueryWrapper<User> Userid = new LambdaQueryWrapper<>();
        Userid.select(User::getId);
        LambdaQueryWrapper<SysUserRole> sysUserid = new LambdaQueryWrapper<>();
        sysUserid.select(SysUserRole::getUserId);

        return false;
    }

    //根据user查询role
    @Override
    public List<SysRole> selectRolesByUserId(User user) {
        //根据user_id查询SysUserRole
        List<SysUserRole> sysUserRoleList = sysUserRoleService.selectByUserId(user.getId());
        ArrayList<SysRole> sysRoleList = new ArrayList<>();
        if(!sysUserRoleList.isEmpty()){
            for (SysUserRole sysUserRole : sysUserRoleList) {
                SysRole sysRole = sysRoleService.getById(sysUserRole.getRoleId());//查询角色
                sysRoleList.add(sysRole);
            }
        }
        return sysRoleList;
    }

    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public LoginUserDto login(User user) {
        //判断获取的 UserDetails 信息中的密码是否和前端提交的密码一致，
        //security调用AuthenticationManager进行认证
        // 如果一致返回一个通过验证了的 UserDetails（LoginUser），如果密码不一致抛出异常
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getJobId(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //把合法的authentication的内容处理或封装到LoginUserDto
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //密码验证成功，根据 user 的 id 查询对应的 SysRole 信息并存入 loginUser
        List<SysRole> sysRoleList = this.selectRolesByUserId(loginUser.getUser());
        List<String> roles = null;
        //sysRoleList 表中取出角色信息{"ROLE_teacher","ROLE_edu_admin"}
        if (sysRoleList != null && sysRoleList.size() > 0) {
            roles = new ArrayList<>();
            for (SysRole sysRole : sysRoleList) {
                roles.add(sysRole.getRoleKey());
            }
            //roles = sysRoleList.stream().map(role -> role.getRoleKey()).collect(Collectors.toList());
            //loginUser.setPermissions(roles);
        }
        //TODO 把user及角色信息存入redis
        LoginUserDto loginUserDto = new LoginUserDto();
        BeanUtil.copyProperties(loginUser.getUser(), loginUserDto, true);
        String token = JwtUtil.creatToken(loginUser.getUser().getId());
        loginUserDto.setToken(token);
        loginUserDto.setRoles(roles);
        return loginUserDto;
    }
}
