package com.woniu.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * ElementType取值有：
 * 1.CONSTRUCTOR:用于描述构造器
 * 2.FIELD:用于描述域
 * 3.LOCAL_VARIABLE:用于描述局部变量
 * 4.METHOD:用于描述方法
 * 5.PACKAGE:用于描述包
 * 6.PARAMETER:用于描述参数
 * 7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 *
 *
 * RetentionPolicy.SOURCE  : 注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
 * RetentionPolicy.CLASS  : 注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
 * RetentionPolicy.RUNTIME  : 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
 *
 * */
@Retention(RetentionPolicy.RUNTIME)//生效时间
@Target(ElementType.TYPE)//生效范围
public @interface ComponentScan {
    String value() default "";
}
