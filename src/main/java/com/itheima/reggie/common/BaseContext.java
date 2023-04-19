package com.itheima.reggie.common;

/**
 * 基于ThreadLocal封装的工具类, 保存用户id和获取用户id
 * @author 陈万三
 * @create 2023-03-15 13:33
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
