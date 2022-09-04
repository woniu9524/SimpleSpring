package com.woniu.service;

import com.woniu.spring.ApplicationContext;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  22:30
 * @Description: 测试类
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
