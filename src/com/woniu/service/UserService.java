package com.woniu.service;

import com.woniu.spring.annotation.Autowired;
import com.woniu.spring.annotation.Component;
import com.woniu.spring.annotation.Scope;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  22:29
 * @Description: User bean
 */

@Scope("prototype")
@Component
public class UserService {
    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println(orderService);
    }
}
