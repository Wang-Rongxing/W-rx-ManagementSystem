package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.entity.Student;
import com.wrx.mapper.StudentMapper;
import com.wrx.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Resource
    private StudentMapper studentMapper;
    //根据考生号，身份证，姓名查询分页查询
    @Override
    public Page<Student> selectByPage(Student student, int pageIndex, int pageSize) {
        LambdaUpdateWrapper<Student> wrapper = new LambdaUpdateWrapper<>();
        if (StringUtils.isNotBlank(student.getSno()))
            wrapper.eq(Student::getSno,student.getSno());
        if (StringUtils.isNotBlank(student.getIdcard()))
            wrapper.eq(Student::getIdcard,student.getIdcard());
        if (StringUtils.isNotBlank(student.getUsername()))
            wrapper.eq(Student::getUsername,student.getUsername());
        Page<Student> studentPage = new Page<>(pageIndex,pageSize);
        return studentMapper.selectPage(studentPage,wrapper);
        //return this.page(studentPage,wrapper);
    }

    @Override
    public Page<Student> selectByPage(int pageIndex, int pageSize) {
        LambdaUpdateWrapper<Student> wrapper = new LambdaUpdateWrapper<>();
        wrapper
                .isNotNull(Student::getResult)
                .ne(Student::getResult," ");
        Page<Student> studentPage = new Page<>(pageIndex,pageSize);
        return this.page(studentPage,wrapper);
    }
}
