package com.woniu.service;

import com.woniu.spring.annotation.Autowired;
import com.woniu.spring.annotation.Component;
import com.woniu.spring.annotation.Scope;
import com.woniu.spring.inter.BeanNameAware;
import com.woniu.spring.inter.InitializingBean;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  22:29
 * @Description: User bean
 */

@Scope("prototype")
@Component
public class UserService implements BeanNameAware , InitializingBean {
    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test() {
        System.out.println(orderService);
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化完成");
    }
}
