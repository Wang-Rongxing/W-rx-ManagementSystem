package com.wrx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.entity.Student;
import com.wrx.service.IStudentService;
import com.wrx.vo.StudentVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@RestController
@CrossOrigin//允许跨域
@RequestMapping("/student")
public class StudentController {
    @Resource
    private IStudentService studentService;
    @PostMapping("/selectStudentsByTest")
    public Page<Student> selectStudentsByTest(@RequestBody StudentVo studentVo){
        Student student = new Student();
        BeanUtils.copyProperties(studentVo,student);
        return studentService.selectByPage(student,studentVo.getPageIndex(),studentVo.getPageSize());
    }
    //考试提交
    @PostMapping("/updateTest")
    public boolean updateTest(@RequestBody Student student){
        //1、提交的时间
        student.setCommittime(LocalDateTime.now());
        //TODO 2、监考老师的id（uid）
        return studentService.updateById(student);
    }
    // 已录入学生的分页查询接口
    @GetMapping("/selectStudentsByTested")
    public Page<Student> selectStudentsByTested(int pageIndex,int pageSize) {
        return studentService.selectByPage(pageIndex,pageSize);
    }

    @DeleteMapping ("/user/{id}")
    public boolean updateTest(@PathVariable String id){
        return studentService.removeById(id);
    }

    @PostMapping ("/selectStudents")
    public Page<Student> selectStudentsByTestd(@RequestBody StudentVo studentVo){
        Student student = new Student();
        BeanUtils.copyProperties(studentVo,student);
        return studentService.selectByPage(student,studentVo.getPageIndex(),studentVo.getPageSize());
    }

}
