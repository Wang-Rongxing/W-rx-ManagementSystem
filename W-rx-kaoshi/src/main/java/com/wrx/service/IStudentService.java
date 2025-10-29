package com.wrx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
public interface IStudentService extends IService<Student> {
    Page<Student> selectByPage(Student student, int pageIndex, int pageSize);
    Page<Student> selectByPage(int pageIndex, int pageSize);
}
