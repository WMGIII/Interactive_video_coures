package com.wmiii.video.utils;

import com.wmiii.video.entity.Teacher;

public class TeacherThreadLocal {
    private TeacherThreadLocal() {

    }

    private static final ThreadLocal<Teacher> THREAD_LOCAL = new ThreadLocal<>();

    public static void put(Teacher teacher) {
        THREAD_LOCAL.set(teacher);
    }

    public static Teacher get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
