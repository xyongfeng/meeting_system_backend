package com.xyongfeng.aop;

import java.lang.annotation.*;


/**
 * 管理日志记录注解
 *
 * @author xyongfeng
 * @since 2023-9-5
 */

@Target(ElementType.METHOD)// 注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)// 注解在哪个阶段执行
@Documented
public @interface OperationLogAnnotation {

    String actionModule(); // 操作模块

    String actionType();  // 操作类型

    String actionUrl();  // 操作路径

    String actionContent() default "";  // 操作内容
}