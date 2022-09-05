package com.woniu.service;

import com.woniu.spring.annotation.Component;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-05  17:22
 * @Description: TODO
 */
@Component
public class BeanPostProcessor implements com.woniu.spring.inter.BeanPostProcessor {
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        System.out.println("====初始化前=====   "+beanName);
    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("====初始化后=====   "+beanName);
    }
}
