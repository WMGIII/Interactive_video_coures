package com.wmiii.video.utils;

import com.wmiii.video.entity.Student;

public class StudentThreadLocal {
    private StudentThreadLocal() {

    }

    private static final ThreadLocal<Student> STUDENT_LOCAL = new ThreadLocal<>();

    public static void put(Student student) {
        STUDENT_LOCAL.set(student);
    }

    public static Student get() {
        return STUDENT_LOCAL.get();
    }

    public static void remove() {
        STUDENT_LOCAL.remove();
    }
}
