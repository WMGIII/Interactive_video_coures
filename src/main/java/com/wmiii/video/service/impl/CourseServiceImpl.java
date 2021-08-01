package com.wmiii.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiniu.http.Error;
import com.wmiii.video.entity.Course;
import com.wmiii.video.entity.Student;
import com.wmiii.video.entity.Teacher;
import com.wmiii.video.mapper.CourseMapper;
import com.wmiii.video.mapper.StudentCourseMapper;
import com.wmiii.video.params.CourseParam;
import com.wmiii.video.params.ErrorCode;
import com.wmiii.video.params.Result;
import com.wmiii.video.params.StudentCourse;
import com.wmiii.video.service.CourseService;
import com.wmiii.video.service.StudentService;
import com.wmiii.video.service.TeacherService;
import com.wmiii.video.utils.StudentThreadLocal;
import com.wmiii.video.utils.TeacherThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherLoginService;

    @Override
    public Result findCourseById(Integer courseId) {
        Course course = this.courseMapper.selectById(courseId);
        return Result.success(course);
    }

    @Override
    public Result findCourseByTeacherId(Integer teacherId) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Course::getCreateDate);
        queryWrapper.eq(Course::getTeacherId, teacherId);
        List<Course> courses = courseMapper.selectList(queryWrapper);

        return Result.success(courses);
    }

    public StudentCourse findStudentCourse(Integer studentId, Integer courseId) {
        LambdaQueryWrapper<StudentCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentCourse::getStudentId, studentId);
        queryWrapper.eq(StudentCourse::getCourseId, courseId);
        queryWrapper.last("limit 1");
        return this.studentCourseMapper.selectOne(queryWrapper);
    }

    public Course findCourseByCourseNameAndTeacher(Integer teacherId, String courseName) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, teacherId);
        queryWrapper.eq(Course::getCourseName, courseName);
        queryWrapper.last("limit 1");
        return this.courseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result submit(CourseParam courseParam, String token) {
        if (StringUtils.isBlank(courseParam.getCourseName()) || StringUtils.isBlank(courseParam.getCourseIntro())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        Teacher teacher = (Teacher)teacherLoginService.findUserByToken(token).getData();
        if (teacher == null) {
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }

        if ((findCourseByCourseNameAndTeacher(teacher.getTeacherId(), courseParam.getCourseName())) != null) {
            return Result.fail(ErrorCode.CREATE_COURSE_FAIL.getCode(), ErrorCode.CREATE_COURSE_FAIL.getMsg());
        }

        Course course = new Course();
        course.setTeacherId(teacher.getTeacherId());
        course.setTeacherName(teacher.getTeacherName());
        course.setCourseName(courseParam.getCourseName());
        course.setCourseIntro(courseParam.getCourseIntro());
        course.setCreateDate(System.currentTimeMillis());
        this.courseMapper.insert(course);

        return Result.success(null);
    }


    @Override
    public Result joinCourse(Integer courseId) {
        Student student = StudentThreadLocal.get();
        if (findStudentCourse(student.getStudentId(), courseId) != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(student.getStudentId());
        studentCourse.setCourseId(courseId);
        this.studentCourseMapper.insert(studentCourse);
        return Result.success(null);
    }


    @Override
    public Result getAllCourses(Integer limit) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Course::getCourseId, -1);
        // queryWrapper.last("limit " + limit);

        return Result.success(courseMapper.selectList(queryWrapper));
    }
}
